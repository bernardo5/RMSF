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
			
			$user = htmlentities($_GET['user'], ENT_QUOTES);
			$time = htmlentities($_GET['time'], ENT_QUOTES);
			$result = $connection->prepare("update users set lastMessageTime=:time where filename = :user;");
			$result->bindParam(':user', $user);
			$result->bindParam(':time', $time);
			$result->execute();
			QueryCheck($result);

			$connection=null;

		?>
	</body>
</html>	
