#!/usr/bin/env python

import server

rm ./uploads/*
server.db.drop_all()
server.db.create_all()
