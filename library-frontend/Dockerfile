FROM node:20-alpine AS build
WORKDIR /app

COPY . .

RUN npm install
RUN npx vitest run
RUN npm run build

FROM nginx:1.27-alpine
COPY --from=build /app/dist/spa /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
