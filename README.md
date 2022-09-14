# AMuseME

Project for ZTI (Zaawansowane Technologie Internetowe) 2022

---

## How to run a project?

1. Open terminal and clone this repo to your local directory
    ```
    git clone https://github.com/bartosz-rogowski/zti-AMuseMe.git
    ```

1. Enter the directory:
    ```
    cd zti-AMuseMe
    ```

1. Create docker containers (*if you do it n-th time - after building the images - you can skip `--build` flag*)
    ``` 
    docker-compose up --build
    ```

1. Enter http://localhost:3000/ in preferred browser

1. In order to stop app, click `Ctrl`+`C` in terminal window and then execute
    ```
    docker-compose down
    ```