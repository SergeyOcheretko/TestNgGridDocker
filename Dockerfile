FROM jenkins/jenkins:lts

USER root

# Устанавливаем docker и docker-compose
RUN apt-get update && \
    apt-get install -y \
      curl \
      gnupg \
      lsb-release \
      docker.io \
      docker-compose && \
    apt-get clean

# Добавляем Jenkins в группу docker (если нужно)
RUN usermod -aG docker jenkins

USER jenkins
