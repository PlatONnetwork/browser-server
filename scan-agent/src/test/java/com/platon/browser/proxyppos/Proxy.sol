pragma solidity ^0.5.13;

/*
 * evm 跨合约调用 Platon系统合约
 * @author hudenian
 * @dev 2020/02/25
*/
contract Proxy {

    function () payable external {
    }

    event ProxyEvent(bytes oneEvent, bytes twoEvent);
    

    function proxySend(bytes memory oneData, address oneAddr, bytes memory twoData, address twoAddr) public {
        uint256 oneLen = oneData.length;
        uint oneRetsize;
        bytes memory oneResval;
        assembly {
            if iszero(call(gas, oneAddr, 0,  add(oneData, 0x20), oneLen, 0, 0)) {
                invalid()
            }
            oneRetsize := returndatasize()
        }
        oneResval = new bytes(oneRetsize);
        assembly {
            returndatacopy(add(oneResval, 0x20), 0, returndatasize())
        }
        
        uint256 twoLen = twoData.length;
        uint twoRetsize;
        bytes memory twoResval;
        assembly {
            if iszero(call(gas, twoAddr, 0,  add(twoData, 0x20), twoLen, 0, 0)) {
                invalid()
            }
            twoRetsize := returndatasize()
        }
        twoResval = new bytes(twoRetsize);
        assembly {
            returndatacopy(add(twoResval, 0x20), 0, returndatasize())
        }

        emit ProxyEvent(oneResval, twoResval);
    }

}
