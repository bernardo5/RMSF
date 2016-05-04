<html>

	<body>
		<?php
			$host = "db.ist.utl.pt";
			$user = "ist175462";
			$pass = "ipka6868";
			$dsn = "mysql:host=$host;dbname=$user";
			function QueryCheck($query){
				if ($query == FALSE){
					$info = $connection->errorInfo();
					echo("<p>Error: {$info[2]}</p>");
					exit();
				}
			}
			try{
				$connection = new PDO($dsn, $user, $pass);
			}
			catch(PDOException $exception){
				echo("<p>Error: ");
				echo($exception->getMessage());
				echo("</p>");
				exit();
			}
			
			
			$user=htmlentities($_GET['user'], ENT_QUOTES);
			$username = htmlentities($_GET['username'], ENT_QUOTES);
			$pass = htmlentities($_GET['pass'], ENT_QUOTES);
			$device = htmlentities($_GET['device'], ENT_QUOTES);
			
			$result = $connection->prepare("insert into users values(:user, :username, :pass, '0');");
			$result->bindParam(':user', $user);
			$result->bindParam(':username', $username);
			$result->bindParam(':pass', $pass);
			$result->execute();
			QueryCheck($result);
			
			$result = $connection->prepare("insert into usersDevices values(:user, :device);");
			$result->bindParam(':user', $user);
			$result->bindParam(':device', $device);
			$result->execute();
			QueryCheck($result);

			$connection=null;

		?>
	</body>
</html>					

					
