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
			//$hash = password_hash($_GET['filePass'] PASSWORD_DEFAULT);
			$hash = htmlentities($_GET['filePass'], ENT_QUOTES);
			
			$username = htmlentities($_GET['username'], ENT_QUOTES);
			$pass = htmlentities($_GET['pass'], ENT_QUOTES);
			$device = htmlentities($_GET['device'], ENT_QUOTES);
			
			
			$result = $connection->prepare("Select filename from users where filename=:user");
			$result->bindParam(':user', $user);
			$result->execute();
			
			if(($result->rowcount())==0){			
				$result = $connection->prepare("insert into users values(:user, :hash, :username, :pass, '0');");
				$result->bindParam(':user', $user);
				$result->bindParam(':hash', password_hash($hash, PASSWORD_DEFAULT));
				$result->bindParam(':username', $username);
				$result->bindParam(':pass', $pass);
				$result->execute();
				QueryCheck($result);
				
				$result = $connection->prepare("insert into usersDevices values(:user, :device);");
				$result->bindParam(':user', $user);
				$result->bindParam(':device', $device);
				$result->execute();
				QueryCheck($result);
				
				$response[0]=1;
			}else{
				$response[0]=0;
			}
			echo json_encode($response);

			$connection=null;

		?>
	</body>
</html>					

					
