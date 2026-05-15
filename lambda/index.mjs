import { BedrockRuntimeClient, ConverseCommand } from "@aws-sdk/client-bedrock-runtime";

const client = new BedrockRuntimeClient({ region: process.env.AWS_REGION || "us-east-1" });
const MODEL_ID = "amazon.nova-pro-v1:0";

export const handler = async (event) => {
    try {
        const body = typeof event.body === "string" ? JSON.parse(event.body) : event.body;
        const { prompt, message, imageBase64, imageMimeType } = body;
        const text = prompt || message;

        if (!text) return response(400, { error: "prompt is required" });

        const content = [];
        if (imageBase64) {
            content.push({
                image: {
                    format: (imageMimeType || "image/jpeg").split("/")[1],
                    source: { bytes: Buffer.from(imageBase64, "base64") }
                }
            });
        }
        content.push({ text });

        const command = new ConverseCommand({
            modelId: MODEL_ID,
            messages: [{ role: "user", content }],
            inferenceConfig: { maxTokens: 1024, temperature: 0.7 }
        });

        const result = await client.send(command);
        const text = result?.output?.message?.content?.[0]?.text || "";
        return response(200, { text });
    } catch (err) {
        console.error(err);
        return response(500, { error: err.message });
    }
};

const response = (statusCode, body) => ({
    statusCode,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
});
