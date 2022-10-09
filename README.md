# NightVerb

This service with the minimalistic UI enables you to create, listen and download "Nightcore" and "Slowed + Reverb" tracks right from YouTube videos.

<p align="center">
  <img src="https://user-images.githubusercontent.com/25826296/194772468-e3dc22dc-492a-412f-8f84-568ccc206559.png">
</p>

## Running

Clone the project and run an app with **Docker Compose**:

```bash
git clone git@github.com:jolice/nightverb.git
cd nightverb
docker-compose pull
docker-compose up --no-build -d
```

After about a minute, you'll be able to access the UI by going to http://localhost:8080/.
If the UI doesn't respond, wait for a few more seconds: probably the backend hasn't been initialized yet.

To stop the service, run the following command in the project directory:

```bash
docker-compose down
```
