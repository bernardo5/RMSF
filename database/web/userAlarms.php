<html>

	<body>
		<?php
			$host = "db.ist.utl.pt";
			$user = "ist175462";
			$pass = "ipka6868";;
			$dsn = "mysql:host=$host;dbname=$user";
			function QueryCheck($query){
				if ($query == FALSE){
					$info = $connection->errorInfo();
					echo("<p>Error: {$info[2]}</p>");
					exit();
				}
			}
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
			$result = $connection->prepare("select alarm from usersAlarms where filename = :username;");
			$result->bindParam(':username', $username);
			$result->execute();
			QueryCheck($result);

			$nrows= $result->rowCount();

			$num=0;
			if($nrows!=0){//user is registered
				foreach($result as $row)
					{
						$response[$num]=$row['alarm'];
						$num=$num+1;
					}
					
			}
			echo json_encode($response);
			$connection=null;

		?>
	</body>
</html>					
