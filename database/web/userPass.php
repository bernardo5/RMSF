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
			$username = $_GET['username'];

			$result = $connection->query("select userName, password from users where fileName='$username';");

			if ($result == FALSE)
			{
				$info = $connection->errorInfo();
				echo("<p>Error: {$info[2]}</p>");
				exit();
			}

			$nrows= $result->rowCount();


			/*if($nrows!=0){//user is registered
				foreach($result as $row)
					{
						$response['user']=1;
						$response['username']=$row['userName'];
						$response['password']=$row['password'];
						//echo json_encode($response);

					}
			}else{
				$response['user']=0;
				echo json_encode($response);
			}*/
			echo "Hello";

		?>
	</body>
</html>					

					