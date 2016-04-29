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

			$result = $connection->query("select alarme from usersAlarms where filename='$username';");

			if ($result == FALSE)
			{
				$info = $connection->errorInfo();
				echo("<p>Error: {$info[2]}</p>");
				exit();
			}

			$nrows= $result->rowCount();

			$num=0;
			if($nrows!=0){//user is registered
				foreach($result as $row)
					{
						$response[$num]=$row['alarme'];
						$num=$num+1;
					}
					echo json_encode($response);
			}
			$connection=null;

		?>
	</body>
</html>					
