pipeline {
  agent any

  environment {
    GRID_URL = "http://localhost:4444/wd/hub"
  }

  stages {
    stage('Clean Previous Containers') {
      steps {
        echo '🧹 Removing old Selenium Grid containers and network...'
        bat 'docker-compose -f docker-compose.yml down --remove-orphans || exit 0'
      }
    }

    stage('Start Selenium Grid') {
      steps {
        echo '🚀 Starting Selenium Grid via Docker Compose...'
        bat 'docker-compose -f docker-compose.yml up -d'
        bat 'ping -n 40 127.0.0.1 > nul' // ~40s pause for node registration
        bat 'curl -s http://localhost:4444/status'
      }
    }

    stage('Run Tests') {
      steps {
        echo '🧪 Cleaning previous Allure results...'
        bat '''
          if exist allure-results (
            rmdir /s /q allure-results
          ) else (
            echo "📁 No previous allure-results to delete"
          )
        '''

        echo '🧪 Running UI tests with Maven...'
        bat 'mvn clean test -Dselenium.grid.url=%GRID_URL%'
      }
    }

    stage('Generate Allure Report') {
      steps {
        echo '📊 Generating Allure report...'
        allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
      }
    }

    stage('Stop Selenium Grid') {
      steps {
        echo '🧹 Shutting down Selenium Grid...'
        bat 'docker-compose -f docker-compose.yml down --remove-orphans'
      }
    }
  }

  post {
    always {
      echo '📦 Archiving test results...'
      junit '**/target/surefire-reports/*.xml'
    }
  }
}
