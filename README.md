Manuel d’utilisation

Lors du lancement de l’application une fenêtre de login apparaît, une base de donnée en local est créée. L’utilisateur doit entrer un nom d’utilisateur ainsi qu’un numéro de port. Si le login n’est pas utilisé on est dirigé vers la page principale sinon un nouveau login est demandé à l’utilisateur. Sur la page principale, l’utilisateur peut se déconnecter, afficher la liste des conversations auxquelles l’utilisateur connecté a participé, ou bien créer une nouvelle conversation. Lorsque l’utilisateur arrive sur la page de création d’une nouvelle conversation, on lui propose la liste des utilisateurs connectés (ceux qui ont répondu au broadcast UDP) ensuite nous avons encore une fois besoin du numéro de port du destinataire.
Pour commencer à dialoguer, l’utilisateur doit afficher les conversations auxquelles il prend part, pour cela il suffit de cliquer sur le bouton “refresh conversation list”. Il lui suffit ensuite de cliquer sur la conversation qu’il souhaite ouvrir. La page de conversation s’ouvre aussi sur le client du destinataire. Vous pouvez maintenant dialoguer au sein de cette conversation en tapant du texte dans la section “enter new message” et cliquer sur envoyer. Pour quitter la conversation, l'utilisateur doit cliquer sur le bouton “back to main frame”.

Installation

Il vous faudra importer les deux bases de données nécessaires au fonctionnement de l'application pour effectuer des tests. Les deux fichiers correspondants sont : LAUNCH/BDD_SETUP/login.sql et LAUNCH/BDD_SETUP/conv_mess.sql.
Il vous faudra donc un serveur local mySQL qui tourne sur votre machine (ex : WAMP). Les paramètres de connexions à mySQL sont par defaut (login : root, mdp :), si vous voulez les changer pour les adapter à vos paramètres de connexions , il faut les changer ligne 22 du fichier POO\src\clientLogin\DatabaseLogin.java et ligne 30 du fichier POO\src\clientClavardage\DatabaseConv_mess.java.
Une fois connecté à phpMyAdmin il faut selectionner "nouvelle base de donnée" puis "importer" et séléctionner un fichier .sql précedemmet cité, faire la même chose pour l'autre fichier .sql.

Afin de lancer le client, il suffit de double cliquer sur le fichier .jar : LAUNCH/DUMAZ_SANCHEZ_poo_project.jar
Lors de l'execution du programme, il vous sera demander d'autoriser l'application à utiliser des fonctionnalités bloquées par le pare-feu. Ces fonctionnalités sont nécessaires à la mise en place de tcp (utilisation des ports) et à la connexion à la base de données.

