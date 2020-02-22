#!/usr/bin/env groovy

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
       println "Hello world"
    def f = new File('file.txt')
    text = f.text
    f.withWriter { w ->
     w << text.replaceAll("var1", ${var1}).replaceAll("var2",${var2}).replaceAll("var3",${var3})
   steps {
      sh "cat file.txt"
   }
   }
}
}
