<html>

	<body>
		<?php
		
			//Verificação de erros durante a Query
			function QueryCheck($query){
				if ($query == FALSE){
					$info = $connection->errorInfo();
					echo("<p>Error: {$info[2]}</p>");
					exit();
				}
			}
			
			$host = "db.ist.utl.pt";
			$user = "ist175573";
			$pass = "swex6595";
			$dsn = "mysql:host=$host;dbname=$user";
			try
			{
				$connection = new PDO($dsn, $user, $pass);
			}
			catch(PDOException $exception)
			{
				echo("<p>Error: ");
				echo($exception->getMessage());
				echo("</p>");
				exit();
			}
			
			
			$user=htmlentities($_GET['user'], ENT_QUOTES);
			$device = htmlentities($_GET['device'], ENT_QUOTES);
			
			$result = $connection->prepare("insert into usersDevices values(:user, :device);");
			$result->bindParam(':user', $user);
			$result->bindParam(':device', $device);
			$result->execute();
			QueryCheck($result);			

			$connection=null;

		?>
	</body>
</html>	
