FROM node:lts-alpine

# install simple http server for serving static content
# RUN npm install -g http-server

WORKDIR /app

COPY package*.json ./

RUN npm install --silent

# copy project files and folders to the current working directory
COPY . .

# build app for production with minification
RUN npm run build

EXPOSE 3000

CMD [ "npm", "run", "serve" ]