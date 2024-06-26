# Правила GitHub Workflow

## Оглавление

1. [Процедура Спринта](#sprint)
   - [Обзор](#obzor)
   - [Sprint review - Обзор спринта](#review)
   - [Sprint planning - Планирование спринта](#planning)
2. [Внесение Изменений](#сontributing)
   - [Наименование ветки](#branch-naming)
   - [Создание pull request](#pull-request)
   - [Мониторинг pull request](#tracking)
   - [Проверка merge](#merge-check)
   - [Завершение](#done)
3. [Работа с GitHub Projects](#description)
   - [Создание задачи](#ticket)
   - [Kanban доска](#kanban)
   - [Спринт](#sprint)

## Процедура спринта <a name="sprint"></a>

В нашей команде мы следуем структурированной процедуре спринта, чтобы обеспечить эффективное сотрудничество и отслеживание прогресса. В этом документе описываются наши спринт активности.

### Обзор <a name="obzor"></a>

Наш процесс спринта состоит из двух основных действий: обзора спринта (sprint review) и планирования спринта (sprint planning). Эти мероприятия происходят в начале каждой недели, обычно по понедельникам.

### Sprint review - Обзор спринта <a name="review"></a>

Обзор спринта служит ретроспективой достижений предыдущего спринта и сеансом планирования предстоящего спринта. Во время этой встречи члены команды обсуждают свой индивидуальный вклад и достижения предыдущего спринта. Обзор спринта включает в себя следующие шаги:

1. **Введение и определение повестки дня.** Лицо, ответственное за повестку дня и регулирование собрания, открывает собрание, определяет повестку дня и следит за тем, чтобы обсуждение продолжалось.

2. **Индивидуальные обновления.** Каждый член команды подробно рассказывает о своем вкладе и работе, выполненной во время предыдущего спринта. Сюда входят успехи, возникшие проблемы и извлеченные уроки.

3. **Документирование результатов спринта.** Назначенный член команды отвечает за документирование результатов обзора спринта. Это включает в себя подведение итогов обсуждений, принятых решений и действий, определенных для предстоящего спринта.

### Sprint planning - Планирование спринта <a name="planning"></a>

После обзора спринта команда приступает к планированию спринта, чтобы определить задачи и цели следующего спринта. Планирование спринта включает в себя следующие шаги:

1. **Просмотр журнала невыполненных задач.** Члены команды просматривают журнал невыполненных задач, который содержит список потенциальных задач, которые необходимо выполнить. Любые незавершенные задачи предыдущего спринта также рассматриваются для включения в предстоящий спринт.

2. **Выбор задач.** Члены команды совместно выбирают задачи из невыполненной работы для включения в предстоящий спринт на основе приоритета, осуществимости и доступных ресурсов.

3. **Назначение задач.** Задачи, выбранные для спринта, назначаются отдельным членам команды в зависимости от их опыта и доступности.

4. **Установка целей спринта.** Команда коллективно определяет цели и задачи предстоящего спринта, обеспечивая их соответствие срокам и приоритетам проекта.


## Внесение Изменений <a name="сontributing"></a>

Перед тем как внести изменения в главную ветку `master`, необходимо пройти через следующие шаги:
1. Создать новую ветку, перед этим посмотри правило [**наименование ветки**](#branch-naming).
2. Создать pull request (PR) в главную ветку, в github это делается [**так**](#pull-request).
3. Начать следить за PR (посмотри для каких [**случаев**](#tracking), какие варианты подойдут): 
   - прикрепить PR к существующему issue, 
   - или создать issue и потом прикрепить PR к issue, 
   - либо добавить PR в github project, 
   - или же создать github issue, добавить PR в github project и прикрепить его к issue.
4. Добавить изменения\коммиты.
5. Запросить [ревью](https://docs.github.com/ru/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/requesting-a-pull-request-review) от разработчиков.
6. После ревью поменять названия и сообщение mergе.
7. Проверь merge по пунктам: [**проверка merge**](#merge-check).
8. Сгруппировать все коммиты (squash commits) и сделать merge.
9. Проверить, что все [**завершено**](#done).

### Наименование ветки <a name="branch-naming"></a>

Новые ветки следуют называть по следующей схеме: `{noun}/{verb_or_noun}`. Таким образом, 
ветви для документации должны быть названы с префиксом `document` и кратким описанием изменения, разделенным дефисом.
Пример: `document/change-srs` значит изменить specification документ. На данный момент выделены
следующие префиксы: `feature`, `fix`, и `document`.

### Создание pull request <a name="pull-request"></a>

С новой ветки необходимо создать PR, чтобы другие разработчики знали, над какими задачами
ведется работа. Создать PR можно [так](https://docs.github.com/ru/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).

- В заголовке PR должно быть краткое описание планируемых изменения.
- В описании PR необходимо подробно описать планируемые изменения и их цель.

### Мониторинг pull request <a name="tracking"></a>

Наша команда использует github projects для мониторинга всех задач. Поэтому нам важно, чтобы
работа с кодом была видна у нас на доске. Мы используем issues для обозначения задач связанных с кодом, issues
могут быть маленькими (small task) и большими (epic). Поэтому для мониторинга PR описаны четыре сценария. 
С правилами работы в нашем github projects можно ознакомиться [здесь](#description).

1. Прикрепить PR к существующему issue.
    
>Если вы хотите закрыть существующий issue, который не является epic (то есть, задача не имеет подзадач), 
то вам необходимо привязать PR к существующему issue.

2. Создать issue и потом прикрепить PR к issue.

>Если вы хотите добавить изменение, которое не представлено в issues, то создайте новую задачу в github project, 
после конвертируйте его в issue. Дальше по инструкции выше прикрепите PR к issue.

>Также возможен другой вариант, он описан ниже.

3. Добавить PR в github project.

>Если вы хотите добавить изменение, которое не представлено в issues, то добавьте PR в github projects. GitHub projects
потом сам добавит PR на доску задач.

 4. Создать github issue, добавить PR в github project и прикрепить его к issue.

>Если вы хотите добавить изменение, которое не представлено в issues, и которое будет частью чего-то большого (модуля, 
epicа), то создайте issue с описанием глобальной задачи (создать модуль, заимлепентировать epic). После прикрепляйте 
свои будущие задачи в виде PR к новому issue.


Важно: при создании PR, закрывающего ранее созданное issue, добавьте ссылку на этот 
issue в описании PR (Например: `Closes #123` или `Fixes: #123`).

### Проверка merge <a name="merge-check"></a>

Перед merge необходимо выполнить следующие пункты:
- получить approve от двух разработчиков,
- проверить merge название на соответствие с форматом `{Verb} {object} {desc}`, где в `desc` содержит информацию по 
issue (`Fixes #123`), который этот merge закрывает. 

### Завершение <a name="done"></a>

В конце следует проверить:
- закрыт ли issue,
- закрыт ли PR,
- завершен ли issue в project.

## Работа с GitHub Projects <a name="description"></a>

### Создание задачи (Ticket)

Для создания новой задачи (Ticket) в GitHub Projects, выполните следующие шаги:
1. Откройте GitHub Projects и выберите соответствующий проект.
2. Нажмите на кнопку "Add cards" в нужной колонке, чтобы создать новую задачу.
3. Заполните информацию о задаче:
   - **Название**: Краткое и описательное название задачи.
   - **Описание**: Подробное описание задачи, включая требования и ожидаемый результат.
   - **Size (размер)**: Используется для оценки сложности задачи. Может быть указан как S (маленький), M (средний) или L (большой), что соответствует story points (1, 3, 5).
   - **Estimate (оценка)**: Предполагаемое количество часов, необходимых для завершения задачи.
   - **Priority (приоритет)**: Уровень важности задачи. Может быть обозначен как P0 (очень важно), P1 (важно) или P2 (менее важно).
   - **Другие метки**: Дополнительные метки, которые могут помочь классифицировать задачу (например, тип задачи, категория и т.д.).

### Kanban доска

Наша Kanban доска состоит из следующих колонок:

- **To Do**: Задачи, которые еще не начаты и ожидают выполнения (backlog проекта).
- **Current Sprint**: Задачи, запланированные на текущий спринт.
- **In Progress**: Задачи, над которыми в данный момент ведется работа.
- **In Review**: Задачи, завершенные и ожидающие проверки или ревью.
- **Done**: Завершенные задачи, готовые к выпуску или отправке в production.

### Спринт планирование

Спринт - это фиксированный период времени, у нас занимает 2 недели, в течение которого команда работает над определенным набором задач. Планирование спринта включает следующие шаги:

1. **Назначение спринт тега**: Каждый тикет должен быть помечен соответствующим тегом спринта, чтобы обозначить, в каком спринте он будет выполнен.
2. **Начало и конец тикета**: Важно отметить дату начала и завершения работы над каждым тикетом в рамках спринта, чтобы учитывать прогресс и планировать следующие шаги.
