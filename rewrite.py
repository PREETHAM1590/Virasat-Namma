import os
import subprocess

def run_cmd(cmd):
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    return result.stdout.strip()

commits = run_cmd('git log --format="%H"').split('\n')
commits.reverse()

for commit in commits:
    print(f'Rewriting {commit}')
    # Extract message
    msg = run_cmd(f'git log --format="%B" -n 1 {commit}')
    
    # Filter message
    new_msg_lines = []
    for line in msg.split('\n'):
        if not line.startswith('Co-authored-by:') and not line.startswith('Co-Authored-By:'):
            new_msg_lines.append(line)
    new_msg = '\n'.join(new_msg_lines)
    
    # Write to temp file
    with open('.temp_msg', 'w', encoding='utf-8') as f:
        f.write(new_msg)
    
    # Since we can't easily rebase everything without resolving conflicts in python, 
    # we can use git filter-branch. We need to reset git filter-branch.
