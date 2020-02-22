#!/usr/bin/env groovy

//def f = new File('file.txt')
/*text = f.text
f.withWriter { w ->
  w << text.replaceAll("var1", "ok").replaceAll("var2","okok").replaceAll("var3","okokok").replaceAll(" ","+")
}*/


pipeline {
    parameters {
        string (name: 'var1',       defaultValue: 'node1',                               description: 'node 1')
        string (name: 'var2',       defaultValue: 'node2',                               description: 'node 2')
        string (name: 'var3',       defaultValue: 'node3',                               description: 'node 3')
    }
    
    
   agent { node { label 'master' } }
   stages {

   stage('Git Clone') { 
   steps {
      git 'https://github.com/bem92/pipeline_jenkins_test.git'
   }      
   }
       
   stage('Build Docker Maven Image') {
   steps {
       echo "test"
       bat "type file.txt"
   }
   }
     
      stage('groovy') {
   steps {
     script {
      def f = new File("$WORKSPACE"'/file.txt')
      text = f.text
      f.withWriter { w ->
        w << text.replaceAll("var1", "ok").replaceAll("var2","okok").replaceAll("var3","okokok").replaceAll(" ","+")
      }
     }
   }
   }
   
   /*stage('Load') {
   code = load 'example.groovy'
  }

  stage('Execute') {
  code.example1()
  }*/
}
}
