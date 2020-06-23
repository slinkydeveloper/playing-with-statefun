################################################################################
#  Licensed to the Apache Software Foundation (ASF) under one
#  or more contributor license agreements.  See the NOTICE file
#  distributed with this work for additional information
#  regarding copyright ownership.  The ASF licenses this file
#  to you under the Apache License, Version 2.0 (the
#  "License"); you may not use this file except in compliance
#  with the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
################################################################################

import sys
import getopt

from messages_pb2 import SeenCount

from kafka import KafkaConsumer

def consume(address):
    consumer = KafkaConsumer('seen', bootstrap_servers=address, auto_offset_reset='earliest', enable_auto_commit=False)

    for message in consumer:
        # message value and key are raw bytes -- decode if necessary!
        # e.g., for unicode: `message.value.decode('utf-8')`
        seen = SeenCount()
        seen.ParseFromString(message.value)
        print("{}:{}:{}: key={} value={}".format(message.topic, message.partition, message.offset, message.key, seen))


def usage():
    print('usage: python3 event-consumer.py --address=localhost:9092')
    sys.exit(1)


def parse_args():
    address = None
    opts, args = getopt.getopt(sys.argv[1:], "a", ["address="])
    for opt, arg in opts:
        if opt in ("-a", "--address"):
            address = arg
    if address is None:
        usage()
    return address


def main():
    address = parse_args()
    print("Going to receive stuff from", address)
    consume(address)


if __name__ == "__main__":
    main()
