pipeline {
  agent any

  environment {
    GRID_URL = "http://localhost:4444/wd/hub"
  }

  stages {
    stage('Clean Previous Containers') {
      steps {
        echo '🧹 Force removing old Selenium Grid containers...'
        bat '''
          docker rm -f selenium-hub || echo "No selenium-hub"
          docker rm -f chrome-node || echo "No chrome-node"
          docker rm -f firefox-node || echo "No firefox-node"
          docker network rm griddockerproject_default || echo "No network"
        '''
      }
    }

    stage('Start Selenium Grid') {
      steps {
        echo '🚀 Starting Selenium Grid via Docker Compose...'
        bat 'docker-compose -f docker-compose.yml down || exit 0'
        bat 'docker-compose -f docker-compose.yml up -d'
        bat 'ping -n 16 127.0.0.1 > nul' // Windows sleep ~15s
        bat 'curl -s http://localhost:4444/status'
      }
    }

    stage('Run Tests') {
      steps {
        echo '🧪 Cleaning previous Allure results...'
        bat '''
          if exist build\\allure-results (
            rmdir /s /q build\\allure-results
          ) else (
            echo "📁 No previous allure-results to delete"
          )
        '''

        echo '🧪 Running UI tests...'
        bat 'gradlew.bat clean test -Dselenium.grid.url=%GRID_URL%'
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
