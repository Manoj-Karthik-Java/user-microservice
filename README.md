To upload a newly created project from your local machine to GitHub, follow these steps:
git init

Add all files in the project directory:
git add .

Commit the added files:
git commit -m "Initial commit"

Link your local repository to the GitHub repository:
git remote add origin https://github.com/your-username/your-repo-name.git

Verify the remote repository:
git remote -v

Push your local repository to GitHub:
giy push -u origin master

If your default branch is named "main" instead of "master," use:
git push -u origin main
