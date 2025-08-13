pipeline {
  agent any

  environment {
    GRID_URL = "http://localhost:4444/wd/hub"
  }

  stages {
    stage('Start Selenium Grid') {
      steps {
        echo '🚀 Starting Selenium Grid via Docker Compose...'
        sh 'docker-compose -f docker-compose.yml up -d'
        sh 'sleep 15' // дать время на регистрацию нод
        sh 'curl -s http://localhost:4444/status | jq .' // проверка Grid
      }
    }

    stage('Run Tests') {
      steps {
        echo '🧪 Running UI tests...'
        sh './gradlew clean test -Dselenium.grid.url=$GRID_URL'
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
        sh 'docker-compose -f docker-compose.yml down'
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
