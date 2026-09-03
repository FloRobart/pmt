#!/bin/sh

# Ce script permet de lancer/mettre à jour la version dockerisée de l'application Pmt.
# Usage :
#   - Pour lancer l'application : ./run.sh
#   - Pour mettre à jour l'application : ./run.sh

docker compose -f docker-compose.yml up -d --force-recreate --build
