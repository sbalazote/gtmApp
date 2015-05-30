set scripts_folder="C:\Users\Sebastian Balazote\workspace\drogueria\src\main\resources\db"

mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\create_schema.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\inserts.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertAffiliate.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertAffiliate2.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertMonodrug.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertDrugCategories.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertBrands.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertProduct.sql"
mysql -uroot -pTPProfesional75.99 -h localhost -D drugstore -v < %scripts_folder%\insertProduct2.sql"

pause