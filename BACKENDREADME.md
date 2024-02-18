## Need help with azure stuff? Your in the right place

```commandline
git pull 
mvn compile
mvn package

sudo systemctl stop kitcomp
sudo systemctl start kitcomp
sudo systemctl restart kitcomp
sudo systemctl status kitcomp

sudo systemctl stop kitcompmock
sudo systemctl start kitcompmock
sudo systemctl restart kitcompmock
sudo systemctl status kitcompmock
```

### Ingredient Disambiguation

`Recipe`s contain `IngredientAmount`s, which have some measure meant
in some system like Imperial or Metric. These classes contain a `Ingredient`,
which store any other data like nutrition so we don't make 100k calls to the usda database