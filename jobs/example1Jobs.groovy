String basePath = 'example1'
String repo = 'sheehan/grails-example'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}

job("$basePath/grails-example-build") {
    scm {
        github repo
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        grails {
            useWrapper true
            targets(['test-app', 'war'])
        }
    }
}

job("$basePath/grails-example-deploy") {
    parameters {
        stringParam 'host'
    }
    steps {
        shell 'scp war file; restart...'
    }
}

buildFlowJob("build-flow") {
    buildFlow('''
        testSuites = build("job1")
        artifacts = testSuites.getArtifacts()
        artifacts.each {
            println "${it}"
        }
    ''')
}

job("job1"){
    steps {
        shell 'touch t605_test_1.xml t605_test_2.xml dusg2_test_1.xml dusg2_test_2.xml'
    }
    publishers {
        archiveArtifacts {
            pattern('*.xml')
        }
    }
}

pipelineJob('Pipeline') {
  definition {
    cps {
      sandbox()
      script("""
        node {
          stage('init') {
            steps {
              sh 'echo "Pipeline-init"'
            }
          } 
          stage('build') {
            steps {
              sh 'echo "Pipeline-build"'
            }
          }
        }
      """.stripIndent())      
    }
  }
}
