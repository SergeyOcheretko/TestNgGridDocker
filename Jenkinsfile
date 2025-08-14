pipeline {
  agent any

  environment {
    GRID_URL = "http://localhost:4444/wd/hub"
  }

  stages {
    stage('Force Kill Containers') {
      steps {
        echo '🔪 Killing stuck Selenium Grid containers...'
        sh '''
          docker kill chrome-node || echo "No chrome-node to kill"
          docker kill firefox-node || echo "No firefox-node to kill"
          docker rm -f chrome-node || echo "Removed chrome-node"
          docker rm -f firefox-node || echo "Removed firefox-node"
          docker rm -f selenium-hub || echo "Removed selenium-hub"
        '''
      }
    }

    stage('Clean Previous Containers') {
      steps {
        echo '🧹 Removing old Selenium Grid containers and network...'
        sh 'docker-compose -f docker-compose.yml down --remove-orphans || true'
      }
    }

    stage('Start Selenium Grid') {
      steps {
        echo '🚀 Starting Selenium Grid via Docker Compose...'
        sh 'docker-compose -f docker-compose.yml up -d'
        echo '⏳ Waiting for node registration...'
        sh 'sleep 40'

        echo '🔍 Checking Grid status...'
        sh '''
          curl -s http://localhost:4444/status > grid-status.json
          if ! grep -q '"ready": true' grid-status.json; then
            echo "❌ Grid is not ready"
            exit 1
          else
            echo "✅ Grid is ready"
          fi
        '''
      }
    }

    stage('Run Tests') {
      steps {
        echo '🧪 Cleaning previous Allure results...'
        sh '''
          if [ -d allure-results ]; then
            rm -rf allure-results
          else
            echo "📁 No previous allure-results to delete"
          fi
        '''

        echo '🧪 Running UI tests with Maven...'
        sh 'mvn clean test -Dselenium.grid.url=$GRID_URL'
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
        sh 'docker-compose -f docker-compose.yml down --remove-orphans'
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
