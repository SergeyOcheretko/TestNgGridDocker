pipeline {
  agent any

  environment {
    GRID_URL = "http://localhost:4444/wd/hub"
  }

  stages {
    stage('Start Selenium Grid') {
      steps {
        echo '🚀 Starting Selenium Grid via Docker Compose...'
        bat 'docker-compose -f docker-compose.yml up -d'
        bat 'sleep 15' // дать время на регистрацию нод
        bat 'curl -s http://localhost:4444/status | jq .' // проверка Grid
      }
    }

    stage('Run Tests') {
      steps {
        echo '🧪 Running UI tests...'
        bat './gradlew clean test -Dselenium.grid.url=$GRID_URL'
      }
    }

    stage('Generate Allure Report') {
      steps {
        echo '📊 Generating Allure report...'
        allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
      }
    }

    stage('Stop Selenium Grid') {
      steps {
        echo '🧹 Shutting down Selenium Grid...'
        bat 'docker-compose -f docker-compose.yml down'
      }
    }
  }

  post {
    always {
      echo '📦 Archiving test results...'
      junit '**/build/test-results/**/*.xml'
    }
  }
}
