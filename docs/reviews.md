# Reviews
Contains review in reverse chronological order.

## Improving initial perception of the project.
Goal: Devs can quickly try the project.

### Proposals
- Add a `Dockerfile` and `compose.yaml` to automate environment setup.
- Add README.md file to the doc folder. Make it your documentation title page.
- Move practice areas links from the main README.md to that title page.
- In the docs folder, rename `md` docs to lowercase.

### Details
The most obvious block to use your project is the absence of clear requirements.
It is not clear what to install. Which DB or Java version should I install?
The second biggest problem is where to run those dependencies.
I doubt anyone will want to install those things on their local machine just
to try the project. I propose using Docker and Docker-compose to setup the environment.
