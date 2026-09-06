# --- Stage 1 : Build ---
FROM node:24-alpine AS build
WORKDIR /app

# Optimisation du cache Docker pour les dépendances
COPY package*.json ./
RUN npm ci

# Copie du code source et compilation de production
COPY . .
RUN npm run build -- --configuration production

# --- Stage 2 : Runtime Nginx ---
FROM nginx:alpine

# Suppression de la configuration par défaut
RUN rm -rf /usr/share/nginx/html/* /etc/nginx/conf.d/default.conf

# Copie de la configuration Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copie des fichiers compilés (Angular génère la sortie dans dist/<nom-du-projet>/browser)
COPY --from=build /app/dist/*/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
