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

			$username = htmlentities($_GET['username'], ENT_QUOTES);

			$result = $connection->prepare("select device from usersDevices where filename = :username;");
			$result->bindParam(':username', $username);
			$result->execute();
			QueryCheck($result);
			
			$nrows= $result->rowCount();

			$num=0;
			if($nrows!=0){//user is registered
				foreach($result as $row)
					{
						$response[$num]=$row['device'];
						$num=$num+1;
					}
					echo json_encode($response);
			}
			$connection=null;

		?>
	</body>
</html>					
