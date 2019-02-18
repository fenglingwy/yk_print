/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 * @lint-ignore-every XPLATJSCOPYRIGHT1
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, TouchableOpacity,ToastAndroid} from 'react-native';
// import BluetoothPrinter from "./android/BluetoothPrinter";
import Print from "react-native-yk-print";

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
      'Double tap R on your keyboard to reload,\n' +
      'Shake or press menu button for dev menu',
});



type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
        <View style={styles.container}>
          <Text style={styles.welcome}>Welcome to React Native!</Text>
          <Text style={styles.instructions}>To get started, edit App.js</Text>
          <Text style={styles.instructions}>{instructions}</Text>
          {/*<TouchableOpacity onPress={() => BluetoothPrinter.connectBluetooth()}*/}
          {/*activeOpacity={0.2} focusedOpacity={0.5}>*/}
          {/*<Text style={styles.btn}>连接打印机</Text>*/}
          {/*</TouchableOpacity>*/}

          <TouchableOpacity onPress={() =>{
            Print.print(1,
                '{"code":0,"message":"","traySerlnoPrintInfo":{"reserved_no":"12345678901234567890",' +
                'tray_serlno:"12345678901234567890","item_num_id":"12345678901234567890",' +
                '"barcode":"12345678901234567890","item_name":"维维熊","qty":"21"}}')
          }

          }                          activeOpacity={0.2} focusedOpacity={0.5}>
            <Text style={styles.btn}>打印入库装箱单</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={() => Print.print(2,
              '{"code":0,"message":"","Barcode":"11111111111","type_name":"新品","last_qty":"123","traySowDtls":[{"zone_id":"S","sow_id":"91","qty":"20"},{"zone_id":"A","sow_id":"11","qty":"21"},{"zone_id":"A","sow_id":"1","qty":"2"},{"zone_id":"C","sow_id":"32","qty":"1"},{"zone_id":"D","sow_id":"11","qty":"211"},{"zone_id":"F","sow_id":"12","qty":"11"}]}'
          )}
                            activeOpacity={0.2} focusedOpacity={0.5}>
            <Text style={styles.btn}>打印箱分货清单</Text>
          </TouchableOpacity>
        </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'right',
    margin: 10,
  },
  instructions: {
    textAlign: 'right',
    color: '#0000ff',
    marginBottom: 5,
  },
  btn: {
    textAlign: 'right',
    color: '#0000ff',
    marginBottom: 5,
    marginTop: 10,
  },
});
