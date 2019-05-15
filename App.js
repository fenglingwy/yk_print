/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 * @lint-ignore-every XPLATJSCOPYRIGHT1
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, TouchableOpacity, ToastAndroid} from 'react-native';
// import BluetoothPrinter from "./android/BluetoothPrinter";
// import Print from "react-native-yk-print";
import Print from "./android/print";


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

                <TouchableOpacity onPress={() => {
                    Print.print(1,
                        '[{"reserved_no":"12345678901234567890",' +
                        'tray_serlno_label:"12345678901234567890","item_num_id":"12345678901234567890","supplyer":"供应商A",' +
                        '"barcode":"12345678901234567890","item_name":"维维熊","qty":"21","main_distribution_type":1}]')
                }

                } activeOpacity={0.2} focusedOpacity={0.5}>
                    <Text style={styles.btn}>打印入库装箱单</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => Print.print(2,
                    '[' +
                    '{"barcode":"11111111111","case_num_id":"11111111111","main_distribution_type":"新品","remain_qty":"123","item_num_id":"12345678901234567890","itemid":"12345678901234567890","sow_zone_qty_list":' +
                    '[' +
                    '{"zone_num_id":"A","sow_num_id":"91","qty":"20"},{"zone_num_id":"C","sow_num_id":"91","qty":"20"},' +
                    '{"zone_num_id":"S","sow_num_id":"91","qty":"20"},{"zone_num_id":"A","sow_num_id":"91","qty":"20"},' +
                    '{"zone_num_id":"S","sow_num_id":"91","qty":"20"}]},' +
                    '{"barcode":"222222222","case_num_id":"2222222","main_distribution_type":"22222","remain_qty":"222","item_num_id":"22222222222222","itemid":"222222222222","sow_zone_qty_list":' +
                    '[' +
                    '{"zone_num_id":"A2","sow_num_id":"91","qty":"20"},{"zone_num_id":"C2","sow_num_id":"91","qty":"20"},' +
                    '{"zone_num_id":"S2","sow_num_id":"91","qty":"20"},{"zone_num_id":"A2","sow_num_id":"91","qty":"20"},' +
                    '{"zone_num_id":"S2","sow_num_id":"91","qty":"20"}]}' +
                    ']'
                )}
                                  activeOpacity={0.2} focusedOpacity={0.5}>
                    <Text style={styles.btn}>打印箱分货清单</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => Print.print(3,
                '{"code": 0,"container_labserlno": "190226000004", "full_message": "", "message": "成功","physical_num_id": 1167,"rec_date": 2019-04-08, ' +
                    '"ship_tran_company":"德邦物流","rec_physical_num_id": 101鼓楼店物理仓101鼓楼店物理仓,"reserved_no": "131902260040001"}'
                    )}
                     activeOpacity={0.2} focusedOpacity={0.5}>
                    <Text style={styles.btn}>打印出库箱唛头</Text>
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
