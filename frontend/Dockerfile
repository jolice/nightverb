FROM node:lts-alpine

RUN npm install -g http-server

WORKDIR /app

COPY package.json ./

RUN npm install

COPY . .

ARG VUE_APP_API_URL
ENV VUE_APP_API_URL=$VUE_APP_API_URL

RUN npm run build

CMD [ "http-server", "dist"]