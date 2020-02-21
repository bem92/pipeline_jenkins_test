// Add red marks
def red(String text){
    return """<font size=3 color="red">${text}</font>"""
}

pipeliner{
    /* Parameters */
    stageChoicesParameters()
    branchParameters()
    defaultParameters()

    stringParam('appVersion', '', "Version de l'application cas différente de version. " + red("Uniquement pour edoc, eldap, editions tierces."))
    boolParam('nouvelEnvStreamserve', false, red("A cocher uniquement dans le cas d'un nouveau serveur streamserve"))
    boolParam('nouvelEnvBDD', false, red("A cocher uniquement dans le cas d'un nouvel environnement avec une nouvelle base (SID) à créer"))
    boolParam('nouvelEnvBDDStreamserve', false, red("A cocher uniquement dans le cas d'un nouvel environnement avec une nouvelle base streamserve (SID) à créer"))
    stringParam('remoteDumpPath', '', 'Emplacement sur la machine distante du dump à importer. Si le dump est décompressé, mettre un "/" à la fin du path')
    boolParam('vanille', false, 'A cocher pour importer des dumps de PROD clients')
    stringParam('listVersion', '', 'Liste version SQL')
    boolParam('datamasking', false, 'Valide le lancement du datamasking')

    /* Fixed parameters */
    fixedParam('purgeOldConfigStreamserve', false) /* Value fixed for socle-streamserve */
    fixedParam('stats', true) /* Value fixed for import-base */
    fixedParam('purgeOldConfigPowercenter', false) /* EME?? */
    fixedParam('purgeOldDomain', false) /* EME?? */
    fixedParam('powercenterApplicationsPackage', true) /* EME?? */
    fixedParam('suivefluidUpdate', true) /* Value fixed for SEF push exploit */
    fixedParam('suivefluidCible', 'production') /* Value fixed for SEF push exploit */
    fixedParam('status', '0') /* Value fixed for switch availability SEF */
    fixedParam('fix_metadata', false) /* Value fixed for Import_base */
    fixedParam('ansibleVersion', '2.5') // Uses ansible version 2.5

    mailNotifications()
    withGroupAccess 'suppression-weblo'

    /* Pipeline */
    stage('Echange clefs SSH depuis Jenkins'){
        if(utils.isBunkerInList(['UEM', 'CGI'])){
            echo 'ssh_keys'
        }
    }
    stage('Poste packaging'){
        echo 'poste-packaging'
    }
    stage('Config système'){
        if(!utils.isBunkerInList(['ES', 'SEOLIS'])){
            echo 'configuration-system'
        }
    }

    stage('Repo IT'){
        if(!utils.isBunkerInList(['ES', 'GEG', 'SEOLIS', 'SMEG', 'CGI'])){
            echo 'repo_it'
        }
    }

    stage('Socle streamserve'){
        if(!utils.isBunkerInList(['ES', 'GEG', 'SEOLIS', 'SMEG', 'CGI'])){
            echo 'socle-streamserve'
        }
    }

    stage('Création BDD'){
        if(!utils.isBunkerInList(['ES', 'GEG', 'SEOLIS', 'SMEG'])){
            echo 'creer-base'
        }
    }
    stage('Socle oracle'){
        if(!utils.isBunkerInList(['ES', 'GEG', 'SMEG'])){
            echo 'socle-oracle'
        }
    }

    // Préparation
    stage('Préparation des livrables'){
        echo 'prepare-livrable'
    }
    stage('Préparation des users'){
        if(!utils.isBunkerInList(['ES'])){
            echo 'prepare-user'
        }
    }

    // Base de données
    stage('Import de la BDD'){
        if(!utils.isBunkerInList(['ES'])){
            echo 'import-base'
        }
    }
    stage('Déploiement SQL Migrator'){
        echo 'deploy-sql-migrator'
    }

    stage('Datamasking'){
        if(!utils.isBunkerInList(['ES'])){
            echo 'datamasking'
        }
    }

    stage('Socle Apache'){
        echo 'socle-apache'
    }

    stage('Socle Haproxy'){
        echo 'socle-haproxy'
    }

    // Powercenter
    stage('Socle powercenter'){
        if(utils.isBunkerInList(['UEM', 'ERDF', 'CGI'])){
            echo 'socle-powercenter'
        }
    }

    // Batchs
    stage('Déploiements des batchs'){
        echo 'deploy-batchs'
    }
    stage('Test des batchs'){
        echo 'batchs-test'
    }

    // Embedded
    stage('Déploiement J2E'){
        echo 'deploy-j2e'
    }

    // StreamServe
    stage('Déploiement Streamserve'){
        if(!utils.isBunkerInList(['ES'])){
            echo 'deploy-streamserve'
        }
    }

    // Montages réseau
    stage('Création PDM'){
        if(!utils.isBunkerInList(['ES', 'SEOLIS', 'SMEG'])){
            echo 'creer-point-de-montage'
        }
    }

    stage('Création partage samba'){
        if(utils.isBunkerInList(['UEM'])){
            echo 'creer-partage-samba'
        }
    }

    // Pivot
    stage('Déploiement pivot'){
        echo 'deploy-pivot'
    }

    // Suivefluid
    stage('Push SEF'){
        echo 'suivefluid_push-exploit'
    }
    stage('Switch availability'){
        echo 'switch_availability_suivefluid-exploit'
    }

}
