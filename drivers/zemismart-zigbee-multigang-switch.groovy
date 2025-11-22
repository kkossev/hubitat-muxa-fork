/* groovylint-disable CompileStatic, DuplicateListLiteral, DuplicateMapLiteral, DuplicateNumberLiteral, DuplicateStringLiteral, ImplicitClosureParameter, ImplicitReturnStatement, LineLength, MethodCount, MethodReturnTypeRequired, PublicMethodsBeforeNonPublicMethods, ReturnNullFromCatchBlock, StaticMethodsBeforeInstanceMethods, UnnecessaryGetter */
/**
 *  Zemismart ZigBee Wall Switch Multi-Gang - Device Driver for Hubitat Elevation hub
 *
 *  https://community.hubitat.com/t/zemismart-zigbee-1-2-3-4-gang-light-switches/21124/36?u=kkossev
 *
 *  Based on Muxa's driver Version 0.2.0 (last updated Feb 5, 2020)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Ver. 0.0.1 2019-08-21 Muxa    - first version
 *  Ver. 0.1.0 2020-02-05 Muxa    - Driver name "Zemismart ZigBee Wall Switch Multi-Gang"
 *  Ver. 0.2.1 2022-02-26 kkossev - TuyaBlackMagic for TS0003 _TZ3000_vjhcenzo
 *  Ver. 0.2.2 2022-02-27 kkossev - TS0004 4-button, logEnable, txtEnable, ping(), intercept cluster: E000 attrId: D001 and D002 exceptions;
 *  Ver. 0.2.3 2022-03-04 kkossev - powerOnState options
 *  Ver. 0.2.4 2022-04-16 kkossev - _TZ3000_w58g68s3 Yagusmart 3 gang zigbee switch fingerprint
 *  Ver. 0.2.5 2022-05-28 kkossev - _TYZB01_Lrjzz1UV Zemismart 3 gang zigbee switch fingerprint; added TS0011 TS0012 TS0013 models and fingerprints; more TS002, TS003, TS004 manufacturers
 *  Ver. 0.2.6 2022-06-03 kkossev - powerOnState and Debug logs improvements; importUrl; singleThreaded
 *  Ver. 0.2.7 2022-06-06 kkossev - command '0B' (command response) bug fix; added Tuya Zugbee mini switch TMZ02L (_TZ3000_txpirhfq); bug fix for TS0011 single-gang switches.
 *  Ver. 0.2.8 2022-07-13 kkossev - added _TZ3000_18ejxno0 and _TZ3000_qewo8dlz fingerprints; added TS0001 wall switches fingerprints; added TS011F 2-gang wall outlets; added switchType configuration
 *  Ver. 0.2.9 2022-09-29 kkossev - added _TZ3000_hhiodade (ZTS-EU_1gang); added TS0001 _TZ3000_oex7egmt; _TZ3000_b9vanmes; _TZ3000_zmy4lslw
 *  Ver. 0.2.10 2022-10-15 kkossev - _TZ3000_hhiodade fingerprint correction; added _TZ3000_ji4araar
 *  Ver. 0.2.11 2022-11-07 kkossev - added _TZ3000_tqlv4ug4
 *  Ver. 0.2.12 2022-11-11 kkossev - added _TZ3000_cfnprab5 (TS011F) Xenon 4-gang + 2 USB extension; _TYZB01_vkwryfdr (TS0115) UseeLink; _TZ3000_udtmrasg (TS0003)
 *  Ver. 0.2.13 2022-11-12 kkossev - tuyaBlackMagic() for Xenon similar to Tuya Metering Plug; _TZ3000_cfnprab5 fingerprint correction; added SiHAS and NodOn switches
 *  Ver. 0.2.14 2022-11-23 kkossev - added 'ledMOode' command; fingerprints critical bug fix.
 *  Ver. 0.2.15 2022-11-23 kkossev - added added _TZ3000_zmy1waw6
 *  Ver. 0.3.0  2023-01-07 kkossev - noBindingButPolling() for _TZ3000_fvh3pjaz _TZ3000_9hpxg80k _TZ3000_wyhuocal
 *  Ver. 0.3.1  2023-01-22 kkossev - restored TS0003 _TZ3000_vjhcenzo fingerprint; added _TZ3000_iwhuhzdo
 *  Ver. 0.4.0  2023-01-22 kkossev - parsing multiple attributes;
 *  Ver. 0.4.1  2023-02-10 kkossev - IntelliJ lint; added _TZ3000_18ejxno0 third fingerprint;
 *  Ver. 0.5.0  2023-03-13 kkossev - removed the Initialize capability and replaced it with a custom command
 *  Ver. 0.5.1  2023-04-15 kkossev - bugfix: initialize() was not called when a new device is paired; added _TZ3000_pfc7i3kt; added TS011F _TZ3000_18ejxno0 (2 gangs); _TZ3000_zmy1waw6 bug fix; added TS011F _TZ3000_yf8iuzil (2 gangs)
 *  Ver. 0.5.2  2023-06-10 kkossev - added TS0002 _TZ3000_5gey1ohx; unschedule all remaining jobs from previous drivers on initialize(); added _TZ3000_zigisuyh
 *  Ver. 0.5.3  2023-10-19 kkossev - added TS0001 _TZ3000_agpdnnyd @Sekenenz; added TS011F _TZ3000_iy2c3n6p @tom.guelker
 *  Ver. 0.5.4  2023-10-29 kkossev - added TS0002 _TZ3000_qcgw8qfa
 *  Ver. 0.6.0  2024-01-14 kkossev - Groovy lint; TS0004 _TZ3000_a37eix1s fingerprint correction @Rafael;
 *  Ver. 0.6.1  2024-01-29 kkossev - added TS011F _TZ3000_pmz6mjyu @g.machado
 *  Ver. 0.7.0  2024-02-29 kkossev - more Groovy lint; E000_D003 exception processing; ignored duplicated on/off events for the parent device; added ping() and rtt measurement;
 *  Ver. 0.7.1  2024-05-01 kkossev - added TS0002 _TZ3000_ruldv5dt MCHOZY 2 channel relay; TS0011 _TZ3000_syoxtjf0
 *  Ver. 1.0.0  2024-05-01 kkossev - first version pushed to HPM
 *  Ver. 1.1.0  2024-05-02 kkossev - added TS0726 _TZ3000_kt6xxa4o; added switchBacklight command; added TS0001 _TZ3000_ovyaisip; TS0001 _TZ3000_4rbqgcuv; TS0002 _TZ3000_kgxej1dv; TS0003 _TZ3000_qxcnwv26;
 *  Ver. 1.1.1  2024-05-05 kkossev - added toggle command; added more TS0726 fingerprints; added TS1002 _TZ3000_xa9g7rxs (a weird device!); added _TZ3000_hznzbl0x _TZ3000_mtnpt6ws _TZ3000_pxfjrzyj _TZ3000_pk8tgtdb _TZ3000_ywubfuvt _TZ3000_yervjnlj _TZ3000_f09j9qjb tnx @Gabriel
 *  Ver. 1.1.2  2024-07-22 hhorigian - added  TS000F _TZ3000_m8f3z8ju, 2 Gang Relay.
 *  Ver. 1.1.3  2024-07-24 kkossev - merged dev. branch to main branch
 *  Ver. 1.1.4  2024-08-10 kkossev - added TS000F _TZ3000_hdc8bbha
 *  Ver. 1.1.4  2024-11-25 hhorigian - addded TS0006 _TZ3000_kw8pmgbe, 6BTN Novadigital Keypad
 *  Ver. 1.1.5  2025-03-05 kkossev - added TS0004 _TZ3000_u3oupgdy
 *  Ver. 1.1.6  2025-11-12 kkossev - proper handling of TS000F _TZ3000_m8f3z8ju switchType
 *  Ver. 1.1.7  2025-11-22 kkossev - (dev. branhncg) add TS0004 _TZ3000_jwoyqycg 
 *
 *                                   TODO: TS000F _TZ3000_m8f3z8ju switchType is not working! :( 
 *                                   TODO: add TS0003 _TZ3000_ly9apzky Mod Escritorio (114) fingerprint profileId:"0104", endpointId:"01", inClusters:"0003,0004,0005,0006,0702,0B04,E000,E001,0000", outClusters:"0019,000A", model:"TS0003", manufacturer:"_TZ3000_ly9apzky", controllerType: "ZGB"
 *                                   TODO: add TS0012 _TZ3000_nik4i1qg Mod Cabec 2_1 (130) fingerprint profileId:"0104", endpointId:"01", inClusters:"0003,0004,0005,0006,E000,E001,0000", outClusters:"0019,000A", model:"TS0012", manufacturer:"_TZ3000_nik4i1qg", controllerType: "ZGB"
 *                                   TODO: automatic logsOff()
 *                                   TODO: add healthCheck
 *                                   TODO: add numberOfGangs setting
 */

import hubitat.device.HubAction
import hubitat.device.Protocol
import groovy.transform.Field
import com.hubitat.app.DeviceWrapper
import com.hubitat.app.ChildDeviceWrapper

static String version() { '1.1.7' }
static String timeStamp() { '2025/11/22 1:09 PM' }

@Field static final Boolean debug = false
@Field static final Integer MAX_PING_MILISECONDS = 10000     // rtt more than 10 seconds will be ignored
@Field static final Integer COMMAND_TIMEOUT = 10             // timeout time in seconds

metadata {
    definition(name: 'Zemismart ZigBee Wall Switch Multi-Gang', namespace: 'muxa', author: 'Muxa', importUrl: 'https://raw.githubusercontent.com/kkossev/hubitat-muxa-fork/development/drivers/zemismart-zigbee-multigang-switch.groovy', singleThreaded: true) {
        capability 'Actuator'
        capability 'Configuration'
        capability 'Refresh'
        capability 'Switch'
        capability 'Health Check'

        command 'toggle'
        command 'powerOnState', [
                [name: 'selecy powerOnState and click on the button above', type: 'ENUM', constraints: ['--- Select ---', 'off', 'on', 'last state'], description: 'Select Power On State']
        ]
        command 'switchType', [
                [name: 'select switchType and click on the button above', type: 'ENUM', constraints: ['--- Select ---', 'toggle', 'state', 'momentary'], description: 'Select Switch Type']     // 0: 'toggle', 1: 'state', 2: 'momentary'
        ]
        command 'ledMode', [
                [name: 'select ledMode and click on the button above', type: 'ENUM', constraints: ['--- Select ---', 'disabled', 'lit when on', 'lit when off'], description: 'Select LED Mode']
        ]
        command 'switchBacklight', [
                [name: 'select switchBacklight and click on the button above', type: 'ENUM', constraints: ['--- Select ---', 'off', 'on'], description: 'Select Switch Backlight']
        ]
        command 'initialize', [
                [name: "Select 'Yes' and click on the button above", type: 'ENUM', description: 're-creates the child devices!', constraints: ['--- Select ---', 'Yes', 'No']]
        ]

        if (debug == true) {
            command 'test', ['string']
        }

        attribute 'rtt', 'number'
        attribute 'powerOnState', 'string'
        attribute 'switchType', 'string'
        attribute 'ledMode', 'string'
        attribute 'switchBacklight', 'string'

        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_npzfdcof', deviceJoinName: 'Tuya Zigbee Switch'            // https://www.aliexpress.com/item/1005002852788275.html
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_hktqahrq', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_mx3vgyea', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_5ng23zjs', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_rmjr4ufz', deviceJoinName: 'Tuya Zigbee Switch IHSW02'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_v7gnj3ad', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_qsp2pwtf', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS000F', manufacturer: '_TZ3000_m9af2l6g', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS000F', manufacturer: '_TZ3000_hdc8bbha', deviceJoinName: 'Tuya Zigbee Switch'            // https://community.hubitat.com/t/tuya-1-gang-switch-module-momentary-mode-again/141497/6?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,E000,EF00', outClusters: '000A,0019', model: 'TS000F', manufacturer: '_TZ3218_hdc8bbha', deviceJoinName: 'Tuya Zigbee Switch'            // https://community.hubitat.com/t/tuya-1-gang-switch-module-momentary-mode-again/141497/6?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_oex7egmt', deviceJoinName: 'Tuya 1 gang Zigbee switch MYQ-KLS01L'        //https://expo.tuya.com/product/601097
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_tqlv4ug4', deviceJoinName: 'GIRIER Tuya ZigBee 3.0 Light Switch Module'  //https://community.hubitat.com/t/girier-tuya-zigbee-3-0-light-switch-module-smart-diy-breaker-1-2-3-4-gang-supports-2-way-control/104546
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0001', manufacturer: '_TZ3000_agpdnnyd', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer:'_TZ3000_ovyaisip', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer:'_TZ3000_4rbqgcuv', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0001', manufacturer: '_TZ3000_pk8tgtdb', deviceJoinName: 'Tuya Zigbee Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: 'Zemismart', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_tas0zemd', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0003,0006,0019', model: 'TS0002', manufacturer: '_TZ3000_qcgw8qfa', deviceJoinName: 'Zemismart 2 gang smart switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_7hp93xpr', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_7hp93xpr', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_vjhyd6ar', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_tonrapsk', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_bvrlqyj7', deviceJoinName: 'Avatto Zigbee Switch Multi-Gang'            // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_atp7xmd9', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'         // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TZ3000_h34ihclt', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'              // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0002', manufacturer: '_TYZB01_wmak4qjy', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'         // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,E000,E001', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_qn8qvk9y', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_b9vanmes', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_tqlv4ug4', deviceJoinName: 'GIRIER Tuya ZigBee 3.0 Light Switch Module'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_zmy4lslw', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0006,0003,0004,0005,E001', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_5gey1ohx', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'     //https://community.hubitat.com/t/mbg-line-tuya-2ch-ln/115309?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_ruldv5dt', deviceJoinName: 'MCHOZY 2 channel' // https://community.hubitat.com/t/little-help-with-a-mhcozy-2-channel-5v-12v-zigbee-smart-relay/135423?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_kgxej1dv', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_hznzbl0x', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_mtnpt6ws', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0002', manufacturer: '_TZ3000_pxfjrzyj', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS0002', manufacturer:'_TZ3000_ywubfuvt', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TYZB01_pdevogdj', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_pdevogdj', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_odzoiovu', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_vsasbzkf', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_34zbimxh', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_wqfdvxen', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_c0wbnbbf', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019', model: 'TS0003', manufacturer: '_TZ3000_c0wbnbbf', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_tbfw3xj0', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_tqlv4ug4', deviceJoinName: 'GIRIER Tuya ZigBee 3.0 Light Switch Module'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_vjhcenzo', deviceJoinName: 'Tuya 3-gang Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '2101,0000', outClusters: '0021', model: 'TS0003', manufacturer: '_TZ3000_udtmrasg', deviceJoinName: 'Tuya 3-gang Switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_iwhuhzdo', deviceJoinName: 'Zemismart ZL-LU03'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_pfc7i3kt', deviceJoinName: 'MOES Tuya Zigebee Module'    // https://community.hubitat.com/t/driver-needed-for-moes-3-gang-smart-switch-module-ms-104cz/116449?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_qxcnwv26', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_yervjnlj', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0003', manufacturer: '_TZ3000_f09j9qjb', deviceJoinName:'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0004', manufacturer: '_TZ3000_ltt60asa', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'        // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0004', manufacturer: '_TZ3000_excgg5kb', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'        // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,E000,E001', outClusters: '0019,000A', model: 'TS0004', manufacturer: '_TZ3000_a37eix1s', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'        // @Rafael
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0004', manufacturer: '_TZ3000_go9rahj5', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001', outClusters: '0019', model: 'TS0004', manufacturer: '_TZ3000_aqgofyol', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0004', manufacturer: '_TZ3000_excgg5kb'        // 4-relays module
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0004', manufacturer: '_TZ3000_w58g68s3'        // Yagusmart 3 gang zigbee switch
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0004', manufacturer: '_TZ3000_tqlv4ug4', deviceJoinName: 'GIRIER Tuya ZigBee 3.0 Light Switch Module'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0004', manufacturer: '_TZ3000_jwoyqycg', deviceJoinName: 'MOES ZigBee 4 Gang Switch'       // https://community.hubitat.com/t/have-moes-zigbee-4-gang-switch-tz3000-jwoyqycg/158965?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0011', manufacturer: '_TZ3000_ybaprszv', deviceJoinName: 'Zemismart Zigbee Switch No Neutral'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_txpirhfq', deviceJoinName: 'Tuya Zigbee Mini Switch TMZ02L'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_hhiodade', deviceJoinName: 'Moes ZTS-EU_1gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_hhiodade', deviceJoinName: 'Moes ZTS-EU_1gang'                    // https://community.hubitat.com/t/uk-moes-zigbee-1-2-3-or-4-gang-light-switch/89747/5?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_ji4araar', deviceJoinName: 'Tuya 1 gang switch'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_9hpxg80k', deviceJoinName: 'Tuya 1 gang'                          // https://github.com/zigpy/zha-device-handlers/issues/535
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0000', outClusters: '0019,000A', model: 'TS0011', manufacturer: '_TZ3000_syoxtjf0', deviceJoinName: 'Tuya 1 gang'                          // https://community.hubitat.com/t/drivers-for-martin-jerry-zigbee-zigbee-smart-switch-us-zb-ss01/119248/18?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0001,0007,0000,0003,0004,0005,0006,E000,E001,0002', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_k008kbls', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'        // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0012', manufacturer: '_TZ3000_uz5xzdgy', deviceJoinName: 'Zemismart Zigbee Switch No Neutral'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_fvh3pjaz', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006,EF00', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_lupfd8zu', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_jl7qyupf', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0000', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_18ejxno0', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,E000,E001', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_18ejxno0', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,E000,E001,0000', outClusters: '0019,000A', model: 'TS0012', manufacturer: '_TZ3000_18ejxno0', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TYZB01_Lrjzz1UV', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'                // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TZ3000_bvrlqyj7', deviceJoinName: 'Avatto Zigbee Switch Multi-Gang'                   // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TZ3000_wu0shw0i', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'                // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TYZB01_stv9a4gy', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'                // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019,000A', model: 'TS0013', manufacturer: '_TZ3000_wyhuocal', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TYZB01_mqel1whf', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TZ3000_fvh3pjaz', deviceJoinName: 'Zemismart Zigbee Switch Multi-Gang'                // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TYZB01_mtlhqn48', deviceJoinName: 'Lonsonho Zigbee Switch Multi-Gang'                 // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: 'TUYATEC-O6SNCwd6', deviceJoinName: 'TUYATEC Zigbee Switch Multi-Gang'                  // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TZ3000_h34ihclt', deviceJoinName: 'Tuya Zigbee Switch Multi-Gang'                     // check!
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006', outClusters: '0019', model: 'TS0013', manufacturer: '_TZ3000_k44bsygw', deviceJoinName: 'Zemismart Zigbee Switch No Neutral'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0000', outClusters: '0019,000A', model: 'TS0013', manufacturer: '_TZ3000_qewo8dlz', deviceJoinName: 'Tuya Zigbee Switch 3 Gang No Neutral'    // @dingyang.yee https://www.aliexpress.com/item/4000298926256.html https://github.com/Koenkk/zigbee2mqtt/issues/6138#issuecomment-774720939
        /* these do NOT work with Hubitat !
        fingerprint profileId:"0104", endpointId:"01", inClusters:"0003,0004,0005,0006,E000,E001,0000", outClusters:"0019,000A", model:"TS011F", manufacturer:"_TZ3000_cfnprab5", deviceJoinName: "Xenon 4-gang + 2 USB extension"    //https://community.hubitat.com/t/xenon-4-gang-2-usb-extension-unable-to-switch-off-individual-sockets/101384/14?u=kkossev
        fingerprint profileId:"0104", endpointId:"01", inClusters:"0000,0006,0003,0004,0005,E001",      outClusters:"0019,000A", model:"TS011F", manufacturer:"_TZ3000_cfnprab5", deviceJoinName: "Xenon 4-gang + 2 USB extension"    //https://community.hubitat.com/t/xenon-4-gang-2-usb-extension-unable-to-switch-off-individual-sockets/101384/14?u=kkossev
        */
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0004,0005,0006', outClusters: '0019,000A', model: 'TS011F', manufacturer: '_TZ3000_zmy1waw6', deviceJoinName: 'Moes 1 gang'                                // https://github.com/zigpy/zha-device-handlers/issues/1262
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS011F', manufacturer: '_TZ3000_18ejxno0', deviceJoinName: 'Moes 2 gang'       // https://pl.aliexpress.com/item/1005002061628356.html
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0003,0004,0005,0006,0702,0B04,E000,E001,0000', outClusters: '0019,000A', model: 'TS011F', manufacturer: '_TZ3000_yf8iuzil', deviceJoinName: 'Moes 2 gang'       // https://community.hubitat.com/t/moes-zigbee-wall-touch-smart-light-switch/97870/36?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_v1pdxuqq', deviceJoinName: 'XH-002P Outlet TS011F No Power Monitoring'  // - no power monitoring !
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_hyfvrar3', deviceJoinName: 'TS011F No Power Monitoring'  // - no power monitoring !
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_cymsnfvf', deviceJoinName: 'TS011F No Power Monitoring'  // - no power monitoring !
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_bfn1w0mm', deviceJoinName: 'TS011F No Power Monitoring'  // - no power monitoring !
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_zigisuyh', deviceJoinName: 'Wall Outlet with USB Universal'  // https://zigbee.blakadder.com/Zemismart_B90.html
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0003,0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_iy2c3n6p', deviceJoinName: 'Tuya Zigbee dual outlet wall socket'  // https://community.hubitat.com/t/dual-zigbee-outlet/126216?u=kkossev
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_pmz6mjyu', deviceJoinName: 'Tuya Zigbee dual outlet wall socket'  // @g.machado
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_vzopcetz', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB (CZ)'
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_vmpbygs5', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB (BS)'
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_4uf3d0ax', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB (FR)'
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_1obwwnmq', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB'
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_oznonj5q', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB'
        fingerprint profileId: '0104', endpointId: '01', inClusters:'0000,0003,0004,0005,0006', outClusters:'0019,000A', model:'TS011F', manufacturer:'_TZ3000_wzauvbcs', deviceJoinName: 'Silvercrest 3 gang switch, with 4 USB (EU)'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,000A,0004,0005,0006', outClusters: '0019', model: 'TS0115', manufacturer: '_TYZB01_vkwryfdr', deviceJoinName: 'UseeLink Power Strip'                       //https://community.hubitat.com/t/another-brick-in-the-wall-tuya-joins-the-zigbee-alliance/44152/28?u=kkossev
        // SiHAS Switch (2~6 Gang)
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'SBM300Z2', deviceJoinName: 'SiHAS Switch 2-gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'SBM300Z3', deviceJoinName: 'SiHAS Switch 3-gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'SBM300Z4', deviceJoinName: 'SiHAS Switch 4-gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'SBM300Z5', deviceJoinName: 'SiHAS Switch 5-gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'SBM300Z6', deviceJoinName: 'SiHAS Switch 6-gang'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0006,0019', outClusters: '0003,0004,0019', manufacturer: 'ShinaSystem', model: 'ISM300Z3', deviceJoinName: 'SiHAS Switch 3-gang'
        // NodOn // https://nodon.pro/en/produits/zigbee-pro-on-off-lighting-relay-switch/
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,0007,0008,1000,FC57', outClusters: '0003,0006,0019', manufacturer: 'NodOn', model: 'SIN-4-2-20', deviceJoinName: 'NodOn Light 2 channels'
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0000,0003,0004,0005,0006,0007,0008,1000,FC57', outClusters: '0003,0006,0019', manufacturer: 'NodOn', model: 'SIN-4-2-20_PRO', deviceJoinName: 'NodOn Light 2 channels'
        // NEW! TS0726 switches + scene buttons
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_kt6xxa4o', deviceJoinName: 'Brazil 3+3 Zigbee Switch'
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_dfl9kueg', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_ml1agdim', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_wsspgtcd', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_fcx5d58u', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_a9buwvb7', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_s678wazd', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_qhyadm57', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_ymcctknk', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZE200_fm5yck8a', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_cipzj0xu', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_gdwja9a7', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_e6r353tf', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_phu8ygaw', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_sal078g8', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        //
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0001,0003,0004,1000,E001', outClusters:'0019,000A,0003,0004,0005,0006,0008,0300,1000', model:'TS1002', manufacturer:'_TZ3000_xa9g7rxs', deviceJoinName: 'Tuya TS1002 switch'
        //
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_dfl9kueg', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_ml1agdim', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_wsspgtcd', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_fcx5d58u', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_a9buwvb7', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_s678wazd', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3000_qhyadm57', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_ymcctknk', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZE200_fm5yck8a', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_cipzj0xu', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_gdwja9a7', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_e6r353tf', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_phu8ygaw', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0003,0004,0005,0006,E000,E001', outClusters:'0019,000A', model:'TS0726', manufacturer:'_TZ3002_sal078g8', deviceJoinName: 'TS0726 switches/scenes'      // not tested
        //
        fingerprint profileId: '0104', endpointId: '01', inClusters: '0004,0005,0006,E000,0000', outClusters: '0019,000A', model: 'TS0006', manufacturer: '_TZ3000_kw8pmgbe', deviceJoinName: 'NovaDigital Zigbee Switch 6BTN' // NOVADIGITAL        
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0000,0001,0003,0004,1000,E001', outClusters:'0019,000A,0003,0004,0005,0006,0008,0300,1000', model:'TS1002', manufacturer:'_TZ3000_xa9g7rxs', deviceJoinName: 'Tuya TS1002 switch'
        fingerprint profileId:'0104', endpointId:'01', inClusters:'0004,0005,0006,E000,E001,0000', outClusters:'0019,000A', model:'TS000F', manufacturer:'_TZ3000_m8f3z8ju', deviceJoinName: 'Tuya Zigbee Switch 2 Gang'        // BRazil 2CH Zigbee
        fingerprint profileId:"0104", endpointId:"01", inClusters:"0003,0004,0005,0006,E000,E001,0000", outClusters:"0019,000A", model:"TS0004", manufacturer:"_TZ3000_u3oupgdy", controllerType: "ZGB", deviceJoinName: 'Tuya Zigbee Switch 2 Gang'        // MHCOZY 4ch Switch
    }
    preferences {
        input(name: 'txtEnable', type: 'bool', title: 'Enable description text logging', defaultValue: true)
        input(name: 'logEnable', type: 'bool', title: 'Enable debug logging', defaultValue: true)
        input(title: 'IMPORTANT', description: '<b>If the device does not operate normally, please pair the device again to HE after changing to this driver!</b>', type: 'paragraph', element: 'paragraph')
    }
}

@Field final Map<Integer, String> SwitchTypeMap = [0: 'toggle', 1: 'state', 2: 'momentary']
@Field final Map<Integer, String> LedModeMap = [0: 'disabled', 1: 'lit when on', 2: 'lit when off']
@Field final Map<Integer, String> PowerOnStateMap = [0: 'off', 1: 'on', 2: 'last state']
@Field final Map<Integer, String> SwitchBacklightMap = [0: 'off', 1: 'on']

/* groovylint-disable-next-line UnusedPrivateMethod */
private boolean isHEProblematic() {
    device.getDataValue('manufacturer') in ['_TZ3000_okaz9tjs', '_TZ3000_r6buo8ba', '_TZ3000_cfnprab5', 'SONOFF', 'Woolley', 'unknown']
}

private boolean noBindingButPolling() {
    device.getDataValue('manufacturer') in ['_TZ3000_fvh3pjaz', '_TZ3000_9hpxg80k', '_TZ3000_wyhuocal']
}    //0x4001 OnTime: value 0 //0x4002 OffWaitTime: value 0

private boolean isTS000F() {
    device.getDataValue('model') == 'TS000F'
}

// Parse incoming device messages to generate events

void parse(String description) {
    checkDriverVersion()
    unschedule('deviceCommandTimeout')
    Map descMap = [:]
    try {
        descMap = zigbee.parseDescriptionAsMap(description)
    }
    catch (e) {
        if (settings?.logEnable) { log.warn "${device.displayName} exception caught while parsing description ${description} \r descMap:  ${descMap}" }
        return
    }
    logDebug "Parsed descMap: ${descMap} (description:${description})"

    if (descMap.attrId != null) {
        //log.trace "parsing descMap.attrId ${descMap.attrId}"
        parseAttributes(descMap)
        return
    }
    else if (descMap?.profileId != null && descMap?.profileId == '0104' && descMap?.clusterId == '0006' && descMap?.command == 'FD') {
        parseCommandFD(descMap)
    } else if (descMap?.clusterId == '0013' && descMap?.profileId != null && descMap?.profileId == '0000') {
        logInfo "device model ${device.data.model}, manufacturer ${device.data.manufacturer} <b>re-joined the network</b> (deviceNetworkId ${device.properties.deviceNetworkId}, zigbeeId ${device.properties.zigbeeId})"
    } else if (descMap?.clusterId == '0006' && descMap?.profileId != null && descMap?.profileId == '0000') {
        logInfo "Match Descriptor Request (deviceNetworkId ${device.properties.deviceNetworkId}, zigbeeId ${device.properties.zigbeeId})"
    } else if (descMap?.clusterId == '8021' && descMap?.profileId != null && descMap?.profileId == '0000') {
        logInfo "Bind Response (deviceNetworkId ${device.properties.deviceNetworkId}, zigbeeId ${device.properties.zigbeeId})"
    } else if (descMap?.profileId != null && descMap?.command == '07') {
        parseConfigureResponse(descMap)
    } else {
        logDebug "${device.displayName} unprocessed EP: ${descMap.sourceEndpoint} cluster: ${descMap.clusterId} command: ${descMap?.command} attrId: ${descMap.attrId}"
    }
}

void parseAttributes(final Map descMap) {
    // attribute report received
    List attrData = [[cluster: descMap.cluster, attrId: descMap.attrId, value: descMap.value, status: descMap.status]]
    descMap.additionalAttrs.each {
        attrData << [cluster: descMap.cluster, attrId: it.attrId, value: it.value, status: it.status]
    }
    attrData.each {
        parseSingleAttribute(it, descMap)
    }
}

private void parseSingleAttribute(final Map it, final Map descMap) {
    //log.trace "parseSingleAttribute :${it}"
    if (it.status == '86') {
        log.warn "${device.displayName} Read attribute response: unsupported Attributte ${it.attrId} cluster ${descMap.cluster}"
        return
    }
    switch (it.cluster) {
        case '0000':
            parseBasicClusterAttribute(it)
            break
        case '0006':
            //log.warn "case cluster 0006"
            switch (it.attrId) {
                case '0000':
                    //log.warn "case cluster 0006 attrId 0000"
                    processOnOff(it, descMap)
                    break
                default:
                    //log.warn "case cluster 0006 attrId ${it.attrId}"
                    processOnOfClusterOtherAttr(it)
                    break
            }
            break
        case '0008':
            if (logEnable) { log.warn "${device.displayName} may be a dimmer? This is not the right driver...cluster:${cluster} attrId ${it.attrId} value:${it.value}" }
            break
        case '0300':
            if (logEnable) { log.warn "${device.displayName} may be a bulb? This is not the right driver...cluster:${cluster} attrId ${it.attrId} value:${it.value}" }
            break
        case '0702':
        case '0B04':
            if (logEnable) { log.warn "${device.displayName} may be a power monitoring socket? This is not the right driver...cluster:${cluster} attrId ${it.attrId} value:${it.value}" }
            break
        case 'E000':
        case 'E001':
            processOnOfClusterOtherAttr(it)
            break
        case 'EF00': // Tuya cluster
            log.warn "${device.displayName} NOT PROCESSED Tuya Cluster EF00 attribute ${it.attrId}\n descMap = ${descMap}"
            break
        case 'FFFD': // TuyaClusterRevision
            if (logEnable) { log.warn "${device.displayName}  Tuya Cluster Revision cluster:${cluster} attrId ${it.attrId} value:${it.value}" }
            break
        case 'FFFE': // AttributeReportingStatus
            if (logEnable) { log.warn "${device.displayName}  Tuya Attribute Reporting Status cluster:${cluster} attrId ${it.attrId} value:${it.value}" }
            break
        default:
            if (logEnable) {
                String respType = (command == '0A') ? 'reportResponse' : 'readAttributeResponse'
                log.warn "${device.displayName} parseAttributes: <b>NOT PROCESSED</b>: <b>cluster ${descMap.cluster}</b> attribite:${it.attrId}, value:${it.value}, encoding:${it.encoding}, respType:${respType}"
            }
            break
    } // it.cluster
}

void parseBasicClusterAttribute(final Map it) {
    // https://github.com/zigbeefordomoticz/Domoticz-Zigbee/blob/6df64ab4656b65ec1a450bd063f71a350c18c92e/Modules/readClusters.py
    switch (it.attrId) {
        case '0000':
            logDebug "ZLC version: ${it.value}"        // default 0x03
            break
        case '0001':
            if (state.states == null) { state.states = [:] }
            if (state.stats == null) { state.stats = [:] }
            if (state.lastTx == null) { state.lastTx = [:] }
            Long now = new Date().getTime()
            boolean isPing = state.states['isPing'] ?: false
            if (isPing) {
                int timeRunning = now.toInteger() - (state.lastTx['pingTime'] ?: '0').toInteger()
                if (timeRunning > 0 && timeRunning < MAX_PING_MILISECONDS) {
                    state.stats['pingsOK'] = (state.stats['pingsOK'] ?: 0) + 1
                    if (timeRunning < safeToInt((state.stats['pingsMin'] ?: '999'))) { state.stats['pingsMin'] = timeRunning }
                    if (timeRunning > safeToInt((state.stats['pingsMax'] ?: '0')))   { state.stats['pingsMax'] = timeRunning }
                    state.stats['pingsAvg'] = approxRollingAverage(safeToDouble(state.stats['pingsAvg']), safeToDouble(timeRunning)) as int
                    sendRttEvent()
                }
                else {
                    logWarn "unexpected ping timeRunning=${timeRunning} "
                }
                state.states['isPing'] = false
            }

            logDebug "Applicaiton version: ${it.value}"    // For example, 0b 01 00 0001 = 1.0.1, where 0x41 is 1.0.1
            break                                                            // https://developer.tuya.com/en/docs/iot-device-dev/tuya-zigbee-lighting-dimmer-swith-access-standard?id=K9ik6zvlvbqyw
        case '0002':
            logDebug "Stack version: ${it.value}"        // default 0x02
            break
        case '0003':
            logDebug "HW version: ${it.value}"        // default 0x01
            break
        case '0004':
            logDebug "Manufacturer name: ${it.value}"
            break
        case '0005':
            logDebug "Model Identifier: ${it.value}"
            break
        case '0006':
            logDebug "DateCode: ${it.value}"
            break
        case '0007':
            logDebug "Power Source: ${it.value}"        // enum8-0x30 default 0x03
            break
        case '4000':    //software build
            logDebug "softwareBuild: ${it.value}"
            //updateDataValue("$LAB softwareBuild",it.value ?: "unknown")
            break
        case 'FFE2':
        case 'FFE4':
            logDebug "Attribite ${it.attrId} : ${it.value}"
            break
        case 'FFFD':    // Cluster Revision (Tuya specific)
            logDebug "Cluster Revision 0xFFFD: ${it.value}"    //uint16 -0x21 default 0x0001
            break
        case 'FFFE':    // Tuya specific
            logDebug "Tuya specific 0xFFFE: ${it.value}"
            break
        default:
            if (logEnable) { log.warn "${device.displayName} parseBasicClusterAttribute cluster:${cluster} UNKNOWN  attrId ${it.attrId} value:${it.value}" }
    }
}

@Field static final int ROLLING_AVERAGE_N = 10
BigDecimal approxRollingAverage(BigDecimal avgPar, BigDecimal newSample) {
    BigDecimal avg = avgPar
    if (avg == null || avg == 0) { avg = newSample }
    avg -= avg / ROLLING_AVERAGE_N
    avg += newSample / ROLLING_AVERAGE_N
    return avg
}

/**
 * sends 'rtt'event (after a ping() command)
 * @param null: calculate the RTT in ms
 *        value: send the text instead ('timeout', 'n/a', etc..)
 * @return none
 */
void sendRttEvent( String value=null) {
    long now = new Date().getTime()
    if (state.stats == null ) { state.stats = [:] }
    if (state.lastTx == null ) { state.lastTx = [:] }
    int timeRunning = now.toInteger() - (state.lastTx['pingTime'] ?: now).toInteger()
    String descriptionText = "Round-trip time is ${timeRunning} ms (min=${state.stats['pingsMin']} max=${state.stats['pingsMax']} average=${state.stats['pingsAvg']})"
    if (value == null) {
        logInfo "${descriptionText}"
        sendEvent(name: 'rtt', value: timeRunning, descriptionText: descriptionText, unit: 'ms', isDigital: true)
    }
    else {
        descriptionText = "Round-trip time : ${value}"
        logInfo "${descriptionText}"
        sendEvent(name: 'rtt', value: value, descriptionText: descriptionText, isDigital: true)
    }
}

void ping() {
    if (state.lastTx == null ) { state.lastTx = [:] }
    state.lastTx['pingTime'] = new Date().getTime()
    if (state.states == null ) { state.states = [:] }
    state.states['isPing'] = true
    scheduleCommandTimeoutCheck()
    sendZigbeeCommands(zigbee.readAttribute(zigbee.BASIC_CLUSTER, 0x01, [:], 0))
    logDebug 'ping...'
}

private void scheduleCommandTimeoutCheck(int delay = COMMAND_TIMEOUT) {
    runIn(delay, 'deviceCommandTimeout')
}

void deviceCommandTimeout() {
    logWarn 'no response received (sleepy device or offline?)'
    sendRttEvent('timeout')
    state.stats['pingsFail'] = (state.stats['pingsFail'] ?: 0) + 1
}

/**
 * Zigbee Configure Reporting Response Parsing  - command 0x07
 */
void parseConfigureResponse(final Map descMap) {
    // TODO - parse the details of the configuration respose - cluster, min, max, delta ...
    final String status = ((List)descMap.data).first()
    final int statusCode = hexStrToUnsignedInt(status)
    final String statusName = statusCode == 0 ? 'Success' : "0x${status}"
    if (statusCode > 0x00) {
        log.warn "zigbee configure reporting error: ${statusName} ${descMap.data}"
    } else {
        if (logEnable) { logInfo "zigbee configure reporting response: ${statusName} ${descMap.data}" }
    }
}

/* groovylint-disable-next-line UnusedMethodParameter */
void processOnOff(final Map it, final Map descMap) {
    // descMap.command =="0A" - switch toggled physically
    // descMap.command =="01" - get switch status
    // descMap.command =="0B" - command response
    ChildDeviceWrapper cd = getChildDevice("${device.id}-${descMap.endpoint}")
    if (cd == null) {
        if (!(device.data.model in ['TS0011', 'TS0001'])) {
            log.warn "${device.displayName} Child device ${device.id}-${descMap.endpoint} not found. Initialise parent device first"
            return
        }
    }
    String switchAttribute = descMap.value == '01' ? 'on' : 'off'
    String descriptionText = ''
    if (cd != null) {
        if (descMap.command in ['0A', '0B']) {
            // switch toggled
            // TS0726 is sendding duplicate messages... : ( - ignore them
            if (cd.currentValue('switch') == switchAttribute) {
                logDebug "switch ${descMap.endpoint} is already in the requested state ${switchAttribute}"
                return
            }
            descriptionText = "Child switch ${descMap.endpoint} turned ${switchAttribute}"
            cd.parse([[name: 'switch', value: switchAttribute, descriptionText: descriptionText]])
            logInfo "${descriptionText}"
        } else if (descMap.command == '01') {
            // report switch status
            descriptionText = "Child switch ${descMap.endpoint} is ${switchAttribute}"
            cd.parse([[name: 'switch', value: switchAttribute, descriptionText: descriptionText]])
            logInfo "${descriptionText}"
        }
    }
    if (switchAttribute == 'on') {
        if (device.currentValue('switch') == 'on') {
            logDebug 'Parent switch is already on'
            return
        }
        sendEvent(name: 'switch', value: 'on')
        logInfo 'Parent switch is on'
        return
    } else if (switchAttribute == 'off') {
        int cdsOn = 0
        // cound number of switches on
        getChildDevices().each { child ->
            if (getChildId(child) != descMap.endpoint && child.currentValue('switch') == 'on') {
                cdsOn++
            }
        }
        if (cdsOn == 0) {
            if (device.currentValue('switch') == 'off') {
                logDebug 'Parent switch is already off'
                return
            }
            sendEvent(name: 'switch', value: 'off')
            logInfo 'Parent switch is off'
            return
        }
    }
}

void parseCommandFD(final Map descMap) {
    if (device.data.model in ['TS0726']) {
        parseTS0726(descMap)
    }
    else if (device.data.model in ['TS1002']) {
        parseTS1002(descMap)
    }
    else {
        logWarn "parseCommandFD: not processed for model ${device.data.model}"
    }
}

void parseTS1002(final Map descMap) {
    if (descMap?.data?.size() != 3) {
        logWarn "parseTS1002: unexpected data size ${descMap?.data?.size()}"
        return
    }
    String strButton = descMap.data[2]
    ChildDeviceWrapper cd = getChildDevice("${device.id}-${strButton}")
    if (cd != null) {
        // toggle the virtual switch
        String switchAttribute = cd.currentValue('switch') == 'on' ? 'off' : 'on'
        String descriptionText = "Scene switch  ${strButton} is ${switchAttribute}"
        cd.parse([[name: 'switch', value: switchAttribute, descriptionText: descriptionText]])
        logInfo "${descriptionText}"
    }
    else {
        if (settings?.txtEnable) { log.warn "parseTS1002: Child device ${device.id}-${strButton} not found. Click on the parent device 'Initialize' button, first selecting 'Yes' in the drop-down options list below" }
    }
}

void parseTS0726(final Map descMap) {
    ChildDeviceWrapper cd = getChildDevice("${device.id}-${descMap.sourceEndpoint}")
    if (cd != null) {
        // toggle the virtual switch
        String switchAttribute = cd.currentValue('switch') == 'on' ? 'off' : 'on'
        String descriptionText = "Scene switch  ${descMap.sourceEndpoint} is ${switchAttribute}"
        cd.parse([[name: 'switch', value: switchAttribute, descriptionText: descriptionText]])
        logInfo "${descriptionText}"
    }
    else {
        if (settings?.txtEnable) { log.warn "Child device ${device.id}-${descMap.sourceEndpoint} not found. Click on the parent device 'Initialize' button, first selecting 'Yes' in the drop-down options list below" }
    }
}

void off() {
    if (settings?.txtEnable) { log.info "${device.displayName} Turning all child switches off" }
    sendZigbeeCommands(["he cmd 0x${device.deviceNetworkId} 0xFF 0x0006 0x0 {}"])
}

void on() {
    if (settings?.txtEnable) { log.info "${device.displayName} Turning all child switches on" }
    sendZigbeeCommands(["he cmd 0x${device.deviceNetworkId} 0xFF 0x0006 0x1 {}"])
}

void toggle() {
    if (settings?.txtEnable) { log.info "${device.displayName} Toggling all child switches" }
    sendZigbeeCommands(["he cmd 0x${device.deviceNetworkId} 0xFF 0x0006 0x2 {}"])
}

void refreshTS0726() {
    logDebug 'refreshing TS0726'
    List<String> cmds = []
    cmds  = zigbee.readAttribute(0x0006, [0x4001, 0x4002, 0x5000, 0x8001, 0x8002], [:], delay = 100)
    cmds += zigbee.readAttribute(0xE000, [0xD004, 0xD005], [:], delay = 100)
    cmds += zigbee.readAttribute(0xE001, [0xD010, 0xD020], [:], delay = 100)
    sendZigbeeCommands(cmds)
}

void refresh() {
    logDebug 'refreshing'
    sendZigbeeCommands(["he rattr 0x${device.deviceNetworkId} 0xFF 0x0006 0x0"])
    if (device.data.model in ['TS0726']) {
        refreshTS0726()
    }
}

/* groovylint-disable-next-line UnusedPrivateMethod */
private Integer convertHexToInt(String hex) {
    Integer.parseInt(hex, 16)
}

private String getChildId(DeviceWrapper childDevice) {
    return childDevice.deviceNetworkId.substring(childDevice.deviceNetworkId.length() - 2)
}

void componentOn(DeviceWrapper childDevice) {
    logDebug "sending componentOn ${childDevice.deviceNetworkId}"
    sendHubCommand(new HubAction("he cmd 0x${device.deviceNetworkId} 0x${getChildId(childDevice)} 0x0006 0x1 {}", Protocol.ZIGBEE))
}

void componentOff(DeviceWrapper childDevice) {
    logDebug "sending componentOff ${childDevice.deviceNetworkId}"
    sendHubCommand(new HubAction("he cmd 0x${device.deviceNetworkId} 0x${getChildId(childDevice)} 0x0006 0x0 {}", Protocol.ZIGBEE))
}

void componentRefresh(DeviceWrapper childDevice) {
    logDebug "sending componentRefresh ${childDevice.deviceNetworkId} ${childDevice}"
    sendHubCommand(new HubAction("he rattr 0x${device.deviceNetworkId} 0x${getChildId(childDevice)} 0x0006 0x0", Protocol.ZIGBEE))
}

void setupChildDevices() {
    logDebug 'Parent setupChildDevices'
    deleteObsoleteChildren()
    int buttons = 0
    int gangs = 0
    switch (device.data.model) {
        case 'TS1002':
            buttons = 8
            break
        case 'SBM300Z6':
            buttons = 6
            break
        case 'TS011F':
            if (device.data.manufacturer == '_TZ3000_zmy1waw6') {
                buttons = 0
            }
            else if (device.data.manufacturer in ['_TZ3000_18ejxno0', '_TZ3000_yf8iuzil', '_TZ3000_iy2c3n6p', '_TZ3000_pmz6mjyu']) {
                buttons = 2
            }
            else if (device.data.manufacturer in ['_TZ3000_wzauvbcs', '_TZ3000_oznonj5q', '_TZ3000_1obwwnmq', '_TZ3000_4uf3d0ax', '_TZ3000_vzopcetz', '_TZ3000_vmpbygs5']) {
                buttons = 3    // LIDL Silvercrest 3 gang switch, with 4 USB (EU, FR, CZ, BS)    // https://github.com/Koenkk/zigbee-herdsman-converters/blob/38bf79304292c380dc8366966aaefb71ca0b03da/src/devices/lidl.ts#L342
            }
            else {
                buttons = 5
            }
            break
        case 'TS0726':  // Brazil 3+3 Zigbee Switch
            buttons = 6
            break
        case 'TS0115':
        case 'SBM300Z5':
            buttons = 5
            break
        case 'TS0004':
        case 'TS0014':
        case 'SBM300Z4':
            buttons = 4
            break
        case 'TS0003':
        case 'TS0013':
        case 'SBM300Z3':
        case 'ISM300Z3':
            buttons = 3
            break
        case 'TS0002':
        case 'TS0012':
        case 'SBM300Z2':
        case 'SIN-4-2-20':
        case 'SIN-4-2-20_PRO':
        case 'TS000F':
            buttons = 2
            break
        case 'TS0011':
        case 'TS0001':
            buttons = 0
            break
        default:
            break
    }
    gangs = buttons == 0 ? 1 : buttons
    state.gangs = gangs
    logInfo "model: ${device.data.model} gangs:${gangs} child devices: ${buttons}"
    createChildDevices((int) buttons)
}

/* groovylint-disable-next-line BuilderMethodWithSideEffects, FactoryMethodName */
void createChildDevices(int buttons) {
    logDebug 'Parent createChildDevices'
    if (buttons == 0) { return }
    for (i in 1..buttons) {
        String childId = "${device.id}-0${i}"
        DeviceWrapper existingChild = getChildDevices()?.find { it.deviceNetworkId == childId }

        if (existingChild) {
            log.info "${device.displayName} Child device ${childId} already exists (${existingChild})"
        } else {
            log.info "${device.displayName} Creatung device ${childId}"
            addChildDevice('hubitat', 'Generic Component Switch', childId, [isComponent: true, name: "Switch EP0${i}", label: "${device.displayName} EP0${i}"])
        }
    }
}

void deleteObsoleteChildren() {
    logDebug 'Parent deleteChildren'
    getChildDevices().each { child ->
        if (!child.deviceNetworkId.startsWith(device.id) || child.deviceNetworkId == "${device.id}-00") {
            log.info "${device.displayName} Deleting ${child.deviceNetworkId}"
            deleteChildDevice(child.deviceNetworkId)
        }
    }
}

static String driverVersionAndTimeStamp() { version() + ' ' + timeStamp() }

void checkDriverVersion() {
    if (state.driverVersion == null || (driverVersionAndTimeStamp() != state.driverVersion)) {
        if (txtEnable == true) { log.debug "${device.displayName} updating the settings from the current driver ${device.properties.typeName} version ${state.driverVersion} to the new version ${driverVersionAndTimeStamp()} [model ${device.data.model}, manufacturer ${device.data.manufacturer}, application ${device.data.application}, endpointId ${device.endpointId}]" }
        initializeVars(fullInit = false)
        state.driverVersion = driverVersionAndTimeStamp()
    }
}

void initializeVars(boolean fullInit = true) {
    if (settings?.txtEnable) { log.info "${device.displayName} InitializeVars()... fullInit = ${fullInit}" }
    if (fullInit == true) {
        unschedule()
        state.clear()
        state.driverVersion = driverVersionAndTimeStamp()
    }
    if (settings?.logEnable == null) { device.updateSetting('logEnable', true) }
    if (settings?.txtEnable == null) { device.updateSetting('txtEnable', true) }
}

void initialize(final String str) {
    if (str == 'Yes') {
        initialize()
    }
    else {
        logInfo "initialize aborted! (str=${str})"
    }
}

void initialize() {
    logDebug 'Initializing...'
    initializeVars(fullInit = true)
    configure()    // added 11/12/2022
    setupChildDevices()
    if (isTS000F()) { device?.deleteCurrentState('ledMode')  }
}

void installed() {
    logInfo "<b>Parent installed</b>, typeName ${device.properties.typeName}, version ${driverVersionAndTimeStamp()}, deviceNetworkId ${device.properties.deviceNetworkId}, zigbeeId ${device.properties.zigbeeId}"
    logInfo "model ${device.data.model}, manufacturer ${device.data.manufacturer}, application ${device.data.application}, endpointId ${device.endpointId}"
    initialize()
}

void updated() {
    logDebug 'Parent updated'
}

List<String> tuyaBlackMagic() {
    List<String> cmds = []
    cmds += zigbee.readAttribute(0x0000, [0x0004, 0x0000, 0x0001, 0x0005, 0x0007, 0xfffe], [:], delay = 200)
    cmds += zigbee.writeAttribute(0x0000, 0xffde, 0x20, 0x0d, [destEndpoint: 0x01], delay = 50)
    return cmds
}

void configure() {
    logDebug ' configure()..'
    List<String> cmds = []
    if (device.data.manufacturer in ['_TZ3000_cfnprab5', '_TZ3000_okaz9tjs']) {
        log.warn "this device ${device.data.manufacturer} is known to NOT work with HE!"
    }
    cmds += tuyaBlackMagic()
    if (noBindingButPolling()) {
        //these  will send out device anounce message at ervery 2 mins as heart beat, setting 0x0099 to 1 will disable it.
        cmds += zigbee.writeAttribute(zigbee.BASIC_CLUSTER, 0x0099, 0x20, 0x01, [mfgCode: 0x0000])
        // Hack : Need to disable reporting for thoses devices, else It will enable a auto power off after 2mn.     // see https://github.com/dresden-elektronik/deconz-rest-plugin/issues/3693
        // https://github.com/Mariano-Github/Edge-Drivers-Beta/blob/652bcfbcf7b8ab8a14557e097b740216760f2b02/zigbee-multi-switch-v4-childs/src/init.lua
        log.warn "disabling ${device.data.manufacturer} device announce message every 2 mins and skipping reporting configuiration!"
        cmds += zigbee.onOffRefresh()
    } else {
        //cmds += refresh()
        cmds += zigbee.onOffConfig()
        cmds += zigbee.onOffRefresh()
    }
    sendZigbeeCommands(cmds)
}

void sendZigbeeCommands(List<String> cmds) {
    logDebug "sendZigbeeCommands : ${cmds}"
    sendHubCommand(new hubitat.device.HubMultiAction(cmds, hubitat.device.Protocol.ZIGBEE))
}

/* groovylint-disable-next-line MethodParameterTypeRequired, NoDef */
static Integer safeToInt(val, Integer defaultVal=0) {
    return "${val}"?.isInteger() ? "${val}".toInteger() : defaultVal
}

/* groovylint-disable-next-line MethodParameterTypeRequired, NoDef, NoDouble */
static Double safeToDouble(val, Double defaultVal=0.0) {
    return "${val}"?.isDouble() ? "${val}".toDouble() : defaultVal
}

/* groovylint-disable-next-line MethodParameterTypeRequired, NoDef */
static BigDecimal safeToBigDecimal(val, BigDecimal defaultVal=0.0) {
    return "${val}"?.isBigDecimal() ? "${val}".toBigDecimal() : defaultVal
}

void logDebug(final String msg) {
    String sDnMsg = device?.displayName + ' ' + msg
    if (settings?.logEnable) { log.debug sDnMsg }
}

void logWarn(final String msg) {
    String sDnMsg = device?.displayName + ' ' + msg
    if (settings?.logEnable) { log.warn sDnMsg }
}

void logInfo(final String msg) {
    String sDnMsg = device?.displayName + ' ' + msg
    if (settings?.txtEnable) { log.info sDnMsg }
}

void powerOnState(final String relayMode) {
    Map<String, Integer> modeMap = PowerOnStateMap.collectEntries { k, v -> [(v): k] }
    if (!modeMap.containsKey(relayMode)) {
        log.error "${device.displayName} please select a Power On State option"
        return
    }
    int modeEnum = modeMap[relayMode]
    logDebug("setting  Power On State option to: ${relayMode}  (${modeEnum}")
    List<String> cmds = zigbee.writeAttribute(0x0006, 0x8002, DataType.ENUM8, modeEnum)
    sendZigbeeCommands(cmds)
}

void switchType(final String type) {
    Map<String, Integer> typeMap = SwitchTypeMap.collectEntries { k, v -> [(v): k] }
    if (!typeMap.containsKey(type)) {
        log.error "${device.displayName} please select a Switch Type option"
        return
    }
    int modeEnum = typeMap[type]
    List<String> cmds = []
    logDebug("setting  Switch Type to: ${type} (${modeEnum})")
    if (isTS000F()) {
        cmds = zigbee.writeAttribute(0x0006, 0x8001, DataType.ENUM8, modeEnum)
        cmds += zigbee.readAttribute(0x0006, 0x8001)
        logDebug "${device.displayName} Switch Type setting for TS000F model"
    }
    else {
        cmds = zigbee.writeAttribute(0xE001, 0xD030, DataType.ENUM8, modeEnum)
        cmds += zigbee.readAttribute(0xE001, 0xD030)
        logDebug "${device.displayName} Switch Type setting for non-TS000F model"
    }
    sendZigbeeCommands(cmds)
}

void ledMode(final String mode) {
    if (isTS000F()) {
        logInfo "${device.displayName} LED mode setting is not supported on TS000F model"
        return
    }
    Map<String, Integer> modeMap = LedModeMap.collectEntries { k, v -> [(v): k] }
    if (!modeMap.containsKey(mode)) {
        log.error "${device.displayName} please select a LED mode option"
        return
    }
    int modeEnum = modeMap[mode]
    logDebug("setting  LED mode option to: ${mode}  (${modeEnum})")
    List<String> cmds = zigbee.writeAttribute(0x0006, 0x8001, DataType.ENUM8, modeEnum)
    sendZigbeeCommands(cmds)
}

void switchBacklight(final String mode) {
    Map<String, Integer> modeMap = SwitchBacklightMap.collectEntries { k, v -> [(v): k] }
    if (!modeMap.containsKey(mode)) {
        log.error "${device.displayName} please select a Switch Backlight option"
        return
    }
    int modeEnum = modeMap[mode]
    logDebug("setting  Switch Backlight option to: ${mode}  (${modeEnum})")
    List<String> cmds = zigbee.writeAttribute(0x0006, 0x5000, DataType.ENUM8, modeEnum)
    sendZigbeeCommands(cmds)
}

void processOnOfClusterOtherAttr(final Map it) {
    logDebug "processOnOfClusterOtherAttr attribute ${it}"
    String mode = null
    String attrName = null
    Integer value = null
    String valueStr = it.value
    Map eventMap = [:]
    try {
        value = Integer.parseInt(it.value)
    }
    catch (e) {
        logDebug "processOnOfClusterOtherAttr: EXCEPTION ${e} while processing attrId: ${it.attrId} value: ${it.value} as Integer. Using valueStr instead."
    }
    String clusterPlusAttr = it.cluster + '_' + it.attrId
    //log.trace "clusterPlusAttr = ${clusterPlusAttr}"
    switch (clusterPlusAttr) {
        case '0006_4001':
            attrName = 'On Time ' + clusterPlusAttr
            mode = value.toString()
            break
        case '0006_4002':
            attrName = 'Off Wait Time ' + clusterPlusAttr
            mode = value.toString()
            break
        case '0006_5000':
            attrName = 'Switch Backlight'
            mode = SwitchBacklightMap[value]
            eventMap = [name: 'switchBacklight', value: mode]
            break
        case '0006_8000':
            attrName = 'Child Lock'
            mode = value == 0 ? 'off' : 'on'
            break
        case '0006_8001':
            if (isTS000F()) {
                attrName = 'Switch Type'
                mode = value == 0 ? 'toggle' : value == 1 ? 'state' : value == 2 ? 'momentary state' : null
                eventMap = [name: 'switchType', value: mode]
            }
            else { 
                attrName = 'LED mode'
                mode = LedModeMap[value]
                eventMap = [name: 'ledMode', value: mode]
            }
            break
        case '0006_8002':
            attrName = 'Power On State'
            mode = PowerOnStateMap[value]
            eventMap = [name: 'powerOnState', value: mode]
            break
        case 'E000_D001':
        case 'E000_D002':
        case 'E000_D003':   // encoding:42, command:0A, value:AAAA,
            attrName = 'attribute ' + clusterPlusAttr
            mode = value == null ? valueStr : value.toString()
            break
        case 'E000_D004':
            attrName = 'Tuya Scene ID ' + clusterPlusAttr
            mode = value == null ? valueStr : value.toString()
            break
        case 'E000_D005':
            attrName = 'Tuya Group ID ' + clusterPlusAttr
            mode = value == null ? valueStr : value.toString()
            break
        case 'E001_D010':
            attrName = 'attribute ' + clusterPlusAttr
            mode = value == null ? valueStr : value.toString()
            break
        case 'E001_D020':
            attrName = 'attribute ' + clusterPlusAttr
            mode = value == null ? valueStr : value.toString()
            break
        case 'E001_D030':
            attrName = 'Switch Type'
            mode = value == 0 ? 'toggle' : value == 1 ? 'state' : value == 2 ? 'momentary state' : null
            eventMap = [name: 'switchType', value: mode]
            break
        default:
            logDebug "processOnOfClusterOtherAttr: <b>UNPROCESSED On/Off Cluster</b>  attrId: ${it.attrId} value: ${it.value}"
            return
    }
    if (logEnable) { log.info "${device.displayName} ${attrName} is: ${mode} (${value != null ? value : '?'})" }
    if (eventMap.size() > 0) {
        eventMap.descriptionText = "${eventMap.name} is ${eventMap.value} (${value != null ? value : '?'})"
        sendEvent(eventMap)
        logInfo "${eventMap.descriptionText}"
    }
}

// Forces the ZCL Disable Default Response bit (0x10) only on EF00/E001 Write Attributes (0x02)
private List<String> noDefaultResponseForTuyaWrites(List<String> cmds) {
    if (!cmds) return cmds
    List<String> out = []
    cmds.each { String cmd -> out << noDefaultResponseForTuyaWriteOne(cmd) }
    return out
}

private String noDefaultResponseForTuyaWriteOne(String cmd) {
    logDebug "noDefaultResponseForTuyaWriteOne: processing cmd='${cmd}'"
    
    if (!cmd) {
        logDebug "noDefaultResponseForTuyaWriteOne: cmd is null or empty, returning as-is"
        return cmd
    }

    // Handle Hubitat 'he wattr' commands for Tuya clusters
    if (cmd.startsWith("he wattr") && (cmd.contains("0xE001") || cmd.contains("0xEF00"))) {
        logDebug "noDefaultResponseForTuyaWriteOne: detected Hubitat wattr command for Tuya cluster"
        // For Hubitat wattr commands, add the disable default response flag at the end
        if (!cmd.contains("0x10")) {
            String modifiedCmd = cmd.replaceFirst(/\{\}$/, "{0x10}")
            logDebug "noDefaultResponseForTuyaWriteOne: modified Hubitat cmd from '${cmd}' to '${modifiedCmd}'"
            return modifiedCmd
        } else {
            logDebug "noDefaultResponseForTuyaWriteOne: Hubitat cmd already has disable default response flag"
            return cmd
        }
    }

    // Only touch commands that mention Tuya clusters EF00/E001
    def clusterMatch = (cmd =~ /(?i)\s0x(E001|EF00)\b/)
    if (!clusterMatch.find()) {
        logDebug "noDefaultResponseForTuyaWriteOne: no Tuya cluster (EF00/E001) found in cmd, returning as-is"
        return cmd
    }
    logDebug "noDefaultResponseForTuyaWriteOne: Tuya cluster detected in cmd"

    // Grab the first {...} which should be the ZCL frame
    def payloadMatcher = (cmd =~ /\{([0-9A-Fa-f ]+)\}/)
    if (!payloadMatcher.find()) {
        logDebug "noDefaultResponseForTuyaWriteOne: no ZCL frame payload found in braces, returning as-is"
        return cmd
    }

    String zclHex = payloadMatcher.group(1).trim()
    logDebug "noDefaultResponseForTuyaWriteOne: extracted ZCL hex payload='${zclHex}'"
    
    List<String> bytes = zclHex.split(/\s+/).findAll { it }
    logDebug "noDefaultResponseForTuyaWriteOne: parsed ${bytes.size()} bytes: ${bytes}"
    
    if (bytes.size() < 4) {
        logDebug "noDefaultResponseForTuyaWriteOne: payload too short (${bytes.size()} < 4 bytes), returning as-is"
        return cmd // not a valid ZCL frame
    }

    try {
        int fc = Integer.parseInt(bytes[0], 16)  // Frame Control
        logDebug "noDefaultResponseForTuyaWriteOne: frame control byte=0x${String.format('%02X', fc)}"
        
        int idx = 1

        // If manufacturer-specific bit is set, skip 2-byte mfg code
        if ((fc & 0x04) != 0) {
            logDebug "noDefaultResponseForTuyaWriteOne: manufacturer-specific bit set, skipping 2-byte mfg code"
            idx += 2
        }

        if (bytes.size() <= idx + 1) {
            logDebug "noDefaultResponseForTuyaWriteOne: insufficient bytes after header (${bytes.size()} <= ${idx + 1}), returning as-is"
            return cmd
        }
        
        /*int tsn =*/ Integer.parseInt(bytes[idx++], 16)
        int cmdId = Integer.parseInt(bytes[idx], 16)
        logDebug "noDefaultResponseForTuyaWriteOne: command ID=0x${String.format('%02X', cmdId)}"

        // Only modify Write Attributes (0x02)
        if (cmdId != 0x02) {
            logDebug "noDefaultResponseForTuyaWriteOne: not a Write Attributes command (0x02), returning as-is"
            return cmd
        }
        
        logDebug "noDefaultResponseForTuyaWriteOne: Write Attributes command detected, modifying frame control"

        // Set Disable Default Response bit (0x10); idempotent
        int originalFc = fc
        fc = fc | 0x10
        bytes[0] = String.format("%02X", fc)
        logDebug "noDefaultResponseForTuyaWriteOne: frame control modified from 0x${String.format('%02X', originalFc)} to 0x${String.format('%02X', fc)}"

        String newPayload = bytes.join(' ')
        String modifiedCmd = cmd.replaceFirst(/\{([0-9A-Fa-f ]+)\}/, "{${newPayload}}")
        logDebug "noDefaultResponseForTuyaWriteOne: original payload='${zclHex}'"
        logDebug "noDefaultResponseForTuyaWriteOne: modified payload='${newPayload}'"
        logDebug "noDefaultResponseForTuyaWriteOne: returning modified cmd='${modifiedCmd}'"
        
        return modifiedCmd
    } catch (Exception e) {
        logDebug "noDefaultResponseForTuyaWriteOne: exception during parsing: ${e.message}, returning original cmd"
        return cmd // fail-safe: keep original if parsing fails
    }
}

void test(final String description) {
    Integer modeEnum = description as Integer
    log.warn "testing <b>${description}</b>"
    //parse(description)
	List<String> cmds = []
    // Construct ZCL frame: FC(0x10=disable default response) + TSN(01) + CMD(02=write attr) + AttrID(30D0) + DataType(30) + Value
    String zclFrame = String.format("10 01 02 30 D0 30 %02X", modeEnum)
    String heRawCmd = "he raw 0x${device.deviceNetworkId} 1 0x01 0xE001 {${zclFrame}}"
    cmds += heRawCmd
    
    // Add read command for the same attribute
    // Construct ZCL frame: FC(0x10=disable default response) + TSN(02) + CMD(00=read attr) + AttrID(30D0)
    String readZclFrame = "10 02 00 30 D0"
    String heReadCmd = "he raw 0x${device.deviceNetworkId} 1 0x01 0xE001 {${readZclFrame}}"
    cmds += heReadCmd
    
    logDebug("test sending cmds: ${cmds}")
    sendZigbeeCommands(cmds)    
}

