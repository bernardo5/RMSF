<html>

	<body>
		<?php
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
			
			$user=$_GET['user'];
			$time = $_GET['time'];
			

			$result = $connection->exec("update users set lastMessageTime='$time' where filename='$user';");

			$connection=null;

		?>
	</body>
</html>	
