Необходимо реализовать консольное CRUD приложение, которое имеет следующие сущности:

Developer (id, firstName, lastName, List<Skill> skills, Specialty specialty)
Skill
Specialty
Status (enum ACTIVE, DELETED)

Developer -> List<Skill> skills + Specialty specialty
Каждая сущность имеет поле Status. В момент удаления, мы не удаляем запись из файла, а меняем её статус на DELETED.

В качестве хранилища данных необходимо использовать текстовые файлы:
developers.json, skills.json, specialties.json

Пользователь в консоли должен иметь возможность создания, получения, редактирования и удаления данных.

Слои:
model - POJO клаcсы
repository - классы, реализующие доступ к текстовым файлам
controller - обработка запросов от пользователя
view - все данные, необходимые для работы с консолью



Например: Developer, DeveloperRepository, DeveloperController, DeveloperView и т.д.


Для репозиторного слоя желательно использовать базовый интерфейс:
interface GenericRepository<T,ID>

interface DeveloperRepository extends GenericRepository<Developer, Long>

class GsonDeveloperRepositoryImpl implements DeveloperRepository

Для работы с json необходимо использовать библиотеку Gson(https://mvnrepository.com/artifact/com.google.code.gson/gson)
Для импорта зависимостей - Maven/Gradle на выбор.

Результатом выполнения задания должен быть отдельный репозиторий с README.md файлом, который содержит описание задачи, проекта.
