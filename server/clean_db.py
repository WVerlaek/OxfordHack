#!/usr/bin/env python

import server

server.db.drop_all()
server.db.create_all()
