cmake_minimum_required(VERSION 3.10.2)
project("sensors")

add_library(sensors SHARED sensors.c)

find_library(log-lib log)
target_link_libraries(sensors ${log-lib})
