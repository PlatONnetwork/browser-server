pragma solidity ^0.5.17;

import "SafeMath.sol";
import "IERC20.sol";

contract ALAT is IERC20 {

    using SafeMath for uint256;

    // ERC20 开始
    uint256                                            supply;
    string public                                      symbol;
    uint8  public                                      decimals;
    string public                                      name;
    mapping (address => uint256)                       balances;
    mapping (address => mapping (address => uint256))  allowed;
    // ERC20 结束

    // Authority 开始
    address public                                     owner;            // 初始资金地址
    address public                                     rewardValueAddr;  // 奖励发放地址
    bool public                                        transferClosed = true;   // 普通用户转账开关
    bool public                                        stopped = false;         // 合约功能开关

    // 是否有转账权限
    modifier transferAuthority() {
        require(!stopped && ( msg.sender == owner || msg.sender == rewardValueAddr || transferClosed == false ) );
        _;
    }

    // 是否有管理权限
    modifier adminAuthority() {
        require(msg.sender == owner);
        _;
    }

    // 是否有转账权限
    modifier registerAuthority() {
        require(!stopped);
        _;
    }

    event LogWithdrawal (address indexed user, uint256 _value);     // 管理台提取金额


    // Authority 结束

    // LAT MAP 开始
    mapping (address => string)  public  keys;
    event LogRegister (address indexed user, string key);
    // LAT MAP 结束

    constructor(uint256 _supply, string memory _symbol, uint8 _decimals, string memory _name, address _rewardValueAddr) public {
        supply = _supply;
        symbol = _symbol;
        decimals = _decimals;
        name = _name;
        rewardValueAddr = _rewardValueAddr;
        owner = msg.sender;
        balances[owner] = _supply;

        emit Transfer(address(uint160(0)), owner, _supply);
    }


    function totalSupply() public view returns (uint256) {
        return supply;
    }
    function balanceOf(address _owner) public view returns (uint256 balance){
        return balances[_owner];
    }
    function transfer(address _to, uint256 _value) public transferAuthority returns (bool success){

        require(balances[msg.sender] >= _value);

        balances[msg.sender] = balances[msg.sender].sub(_value);
        balances[_to] = balances[_to].add(_value);

        emit Transfer(msg.sender, _to, _value);

        return true;
    }
    function transferFrom(address _from, address _to, uint256 _value) public transferAuthority returns (bool success){
        require(balances[_from] >= _value);
        require(allowed[_from][msg.sender] >= _value);

        allowed[_from][msg.sender] = allowed[_from][msg.sender].sub(_value);
        balances[_from] = balances[_from].sub(_value);
        balances[_to] = balances[_to].add(_value);

        emit Transfer(_from, _to, _value);
        return true;
    }
    function approve(address _spender, uint256 _value) public transferAuthority returns (bool success) {
        allowed[msg.sender][_spender] = _value;

        emit Approval(msg.sender, _spender, _value);

        return true;
    }
    function allowance(address _owner, address _spender) public view returns (uint256 remaining) {
        return allowed[_owner][_spender];
    }
    // ERC20 结束

    // ERC20 增强开始
    function stop() public adminAuthority {
        stopped = true;
    }
    function start() public adminAuthority {
        stopped = false;
    }

    function closeTransfer() public adminAuthority{
        transferClosed = true;
    }
    function openTransfer() public adminAuthority {
        transferClosed = false;
    }
    function register(string memory key) public registerAuthority {
        require(bytes(key).length <= 200);  // 目前映射长度42个字符既可。

        keys[msg.sender] = key;
        emit LogRegister(msg.sender, key);
    }
    // ERC20 增强结束

    // 提取合约金额接口
    function withdrawal(uint256 value) public adminAuthority {
        require(address(this).balance >= value);

        msg.sender.transfer(value);

        emit LogWithdrawal(msg.sender, value);
    }

//     // 临时测试
//    function() external payable {}
}