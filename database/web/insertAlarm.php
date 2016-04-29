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
			$alarm = htmlentities($_GET['Alarm'], ENT_QUOTES);
			
			$result = $connection->prepare("insert into usersAlarms values(:user, :alarm);");
			$result->bindParam(':username', $username);
			$result->bindParam(':alarm', $alarm);
			$result->execute();
			QueryCheck($result);
			
			$connection=null;

		?>
	</body>
</html>	
