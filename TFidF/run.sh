#!/bin/bash

java -cp -Xbootclasspath/a:./target/TesteJCStress.jar -XX:+UnlockDiagnosticVMOptions -XX:+WhiteBoxAPI -XX:-RestrictContended -jar target/TesteJCStress.jar -time 5000


