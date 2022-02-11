<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Log in to chat</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="col-4 offset-4 mt-5">
        <h3>Please enter your name</h3>
        <form action="chat" method="post">
            <input class="form-control" type="text" name="name">
            <input class="form-control mt-1" type="submit" value="Login">
        </form>
    </div>
</div>
</body>
</html>