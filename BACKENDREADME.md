## Need help with azure stuff? Your in the right place
Suggested Flow
```commandline
cd /kitchencompanion/backend
git pull
sudo systemctl stop kitcomp
mvn clean
mvn package -DskipTests=true
sudo systemctl start kitcomp
```


All Commands
```commandline
git pull 
mvn compile
mvn package -DskipTests=true

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

Standard Units
```
0: invalid
1: g
2: ml
```

Will pick first to show up under search, if none, default to 0