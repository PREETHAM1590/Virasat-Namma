# Virasat Nova Pro Lambda — Deploy Guide

## 1. Create Lambda Function

```bash
# Zip the function
cd lambda
zip function.zip index.mjs

# Create Lambda (replace ACCOUNT_ID and ROLE_ARN)
aws lambda create-function \
  --function-name virasat-nova-proxy \
  --runtime nodejs22.x \
  --handler index.handler \
  --zip-file fileb://function.zip \
  --role arn:aws:iam::ACCOUNT_ID:role/virasat-lambda-role \
  --timeout 30 \
  --memory-size 256 \
  --region us-east-1
```

## 2. Create IAM Role for Lambda

The Lambda needs permission to call Bedrock. Attach this policy to the role:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "bedrock:InvokeModel",
      "Resource": "arn:aws:bedrock:us-east-1::foundation-model/amazon.nova-pro-v1:0"
    }
  ]
}
```

Also attach `AWSLambdaBasicExecutionRole` for CloudWatch logs.

## 3. Create API Gateway (HTTP API)

```bash
# Create HTTP API
aws apigatewayv2 create-api \
  --name virasat-api \
  --protocol-type HTTP \
  --region us-east-1

# Note the ApiId from output, then create integration + route + deploy
# Or use AWS Console: API Gateway → Create API → HTTP API → Add Lambda integration
```

**Easiest via Console:**
1. API Gateway → Create API → HTTP API
2. Add integration → Lambda → select `virasat-nova-proxy`
3. Route: `POST /chat`
4. Deploy → copy the Invoke URL

## 4. Enable Nova Pro in Bedrock

AWS Console → Bedrock → Model access → Request access for **Amazon Nova Pro**
(Usually instant approval)

## 5. Add URL to Android app

In `local.properties`:
```
novaApiUrl=https://YOUR_API_ID.execute-api.us-east-1.amazonaws.com/chat
```

## 6. Security (Optional but recommended)

Add an API key to API Gateway and pass it as `x-api-key` header in the Android app to prevent unauthorized use of your endpoint.
