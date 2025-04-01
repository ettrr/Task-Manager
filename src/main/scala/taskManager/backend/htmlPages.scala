package taskManager.backend

object HtmlRegisterPage {

  def get: String =
    HtmlRegisterPageNewUser.get("")

}

object HtmlRegisterPageNewUser {

  def get(token: String): String =
    s"""
       |<!DOCTYPE html>
       |<html lang="ru">
       |<head>
       |    <meta charset="UTF-8">
       |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
       |    <title>Регистрация</title>
       |    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
       |    <style>
       |        body {
       |            font-family: Arial, sans-serif;
       |            background-color: #f4f4f4;
       |            margin: 0;
       |            padding: 0;
       |            display: flex;
       |            justify-content: center;
       |            align-items: center;
       |            height: 100vh;
       |        }
       |    </style>
       |</head>
       |<body>
       |    <div class="container">
       |        <h1>Войти по ключу</h1>
       |        <form action="/api/add_task_page" method="post">
       |            <div class="form-group">
       |                <label for="existingToken">Ваш ID токен:</label>
       |                <input type="text" class="form-control" name="userToken" placeholder="your ID token" required>
       |            </div>
       |            <button type="submit" class="btn btn-primary btn-block">Войти</button>
       |        </form>
       |        <h1>Создать ключ, если еще не были зарегистрированы</h1>
       |        <form action="/api/register_new_user" method="post">
       |            <div class="form-group">
       |                <label for="login">Логин:</label>
       |                <input type="text" class="form-control" name="login" placeholder="Login" required>
       |            </div>
       |            <div class="form-group">
       |                <label for="password">Пароль:</label>
       |                <input type="password" class="form-control" name="password" placeholder="Password" required>
       |            </div>
       |            <button type="submit" class="btn btn-primary btn-block">Создать ключ</button>
       |            <div>
       |                <label for="userToken">Ключ:</label>
       |                <input type="text" class="form-control" name="userToken" value="$token" readonly>
       |            </div>
       |        </form>
       |    </div>
       |</body>
       |</html>
       |""".stripMargin

}

object HtmlAddPage {

  def get(inputTable: String, token: String): String = {
    s"""<!DOCTYPE html>
       |<html lang="ru">
       |<head>
       |    <meta charset="UTF-8">
       |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
       |    <title>Добавить задачу</title>
       |    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
       |    <style>
       |        body {
       |            font-family: Arial, sans-serif;
       |            background-color: #f4f4f4;
       |            margin: 0;
       |            padding: 0;
       |            display: flex;
       |            justify-content: center;
       |            align-items: center;
       |            margin-top: 20px;
       |        }
       |        .table-container {
       |            margin-top: 20px;
       |        }
       |        table {
       |            width: 100%;
       |            border-collapse: collapse;
       |        }
       |        th, td {
       |            padding: 8px;
       |            text-align: left;
       |            border-bottom: 1px solid #ddd;
       |            white-space: nowrap;
       |        }
       |        th {
       |            background-color: #f2f2f2;
       |        }
       |    </style>
       |</head>
       |<body>
       |    <div class="container">
       |        <div class="row">
       |            <div class="col-md-6">
       |                <h1>Добавить задачу</h1>
       |                <form action="/api/add_task" method="post">
       |                    <div class="form-group">
       |                        <input type="hidden" class="form-control" name="userToken" value=$token>
       |                    </div>
       |                    <div class="form-group">
       |                        <input type="text" class="form-control" name="title" placeholder="Название" required>
       |                    </div>
       |                    <div class="form-group">
       |                        <label for="datetime">Введите дедлайн и время (ДД-ММ-ГГГГ-ЧЧ:ММ):</label>
       |                        <input
       |                            type="datetime-local"
       |                            class="form-control"
       |                            name="deadline"
       |                            required
       |                        />
       |                    </div>
       |                    <div class="form-group">
       |                        <input type="number" class="form-control" name="duration" placeholder="Продолжительность, в часах" required>
       |                    </div>
       |                    <div class="form-group">
       |                        <input type="text" class="form-control" name="link" placeholder="Ссылка(необязательно)">
       |                    </div>
       |                    <button type="submit" class="btn btn-primary btn-block">Добавить задачу</button>
       |                </form>
       |                <h1>Удалить задачу</h1>
       |                <form action="/api/delete_task" method="post">
       |                    <div class="form-group">
       |                        <input type="hidden" class="form-control" name="userToken" value=$token>
       |                    </div>
       |                    <div class="form-group">
       |                        <input type="number" class="form-control" name="taskID" placeholder="ID задачи для удаления" required>
       |                    </div>
       |                    <button type="submit" class="btn btn-danger btn-block">Удалить задачу</button>
       |                </form>
       |                <h1>Получить расписание</h1>
       |                <form action="/api/get_planing" method="post">
       |                    <div class="form-group">
       |                        <input type="hidden" class="form-control" name="userToken" value=$token>
       |                    </div>
       |                    <button type="submit" class="btn btn-primary btn-block">Составить расписание</button>
       |                 </form>
       |            </div>
       |            <div class="col-md-6">
       |                <h1>Лента задач</h1>
       |                <div class="table-container">
       |                    <table class="table table-striped">
       |                        <thead>
       |                            <tr>
       |                                <th>ID</th>
       |                                <th>Название</th>
       |                                <th>Дедлайн</th>
       |                                <th>Продолжительность</th>
       |                                <th>Ссылка</th>
       |                            </tr>
       |                        </thead>
       |                        <tbody>
       |                            $inputTable
       |                        </tbody>
       |                    </table>
       |                </div>
       |            </div>
       |        </div>
       |    </div>
       |</body>
       |</html>""".stripMargin
  }

}

//<input type="text" class="form-control" name="deadline" placeholder="Дедлайн" required>

object HtmlPlanPage {

  def get(inputTable: String, token: String): String = {
    s"""<!DOCTYPE html>
       |<html lang="ru">
       |<head>
       |    <meta charset="UTF-8">
       |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
       |    <title>Список задач</title>
       |    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
       |    <style>
       |        body {
       |            font-family: Arial, sans-serif;
       |            background-color: #f4f4f4;
       |            margin: 0;
       |            padding: 0;
       |            display: flex;
       |            justify-content: center;
       |            align-items: center;
       |            height: 100vh;
       |        }
       |        table {
       |            width: 100%;
       |            border-collapse: collapse;
       |        }
       |        th, td {
       |            padding: 8px;
       |            text-align: left;
       |            border-bottom: 1px solid #ddd;
       |        }
       |        th {
       |            background-color: #f2f2f2;
       |        }
       |    </style>
       |</head>
       |<body>
       |    <div class="container">
       |        <h1>План выполнения задач</h1>
       |        <div class="table-container">
       |            <table class="table table-striped">
       |                <thead>
       |                    <tr>
       |                        <th>Название</th>
       |                        <th>Дедлайн</th>
       |                        <th>Продолжительность</th>
       |                        <th>Ссылка</th>
       |                    </tr>
       |                </thead>
       |                <tbody>
       |                    $inputTable
       |                </tbody>
       |            </table>
       |        </div>
       |        <form action="/api/add_task_page" method="post">
       |            <div class="form-group">
       |                <input type="hidden" class="form-control" name="userToken" value=$token>
       |            </div>
       |            <button type="submit" class="btn btn-primary btn-block">Добавить задачу</button>
       |        </form>
       |    </div>
       |</body>
       |</html>""".stripMargin
  }

}
