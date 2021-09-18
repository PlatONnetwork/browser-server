package com.platon.browser.v0152.contract;

import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.FunctionEncoder;
import com.platon.abi.solidity.TypeReference;
import com.platon.abi.solidity.datatypes.*;
import com.platon.abi.solidity.datatypes.generated.Bytes4;
import com.platon.abi.solidity.datatypes.generated.Uint256;
import com.platon.crypto.Credentials;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.PlatonFilter;
import com.platon.protocol.core.methods.response.Log;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Contract;
import com.platon.tx.TransactionManager;
import com.platon.tx.gas.GasProvider;
import rx.Observable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://github.com/PlatONnetwork/client-sdk-java/releases">platon-web3j command line tools</a>,
 * or the com.alaya.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/PlatONnetwork/client-sdk-java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 0.13.2.1.
 */
public class Erc721Contract extends Contract implements ErcContract {

    private static final String BINARY = "60806040523480156200001157600080fd5b506040516200239038038062002390833981810160405260408110156200003757600080fd5b81019080805160405193929190846401000000008211156200005857600080fd5b9083019060208201858111156200006e57600080fd5b82516401000000008111828201881017156200008957600080fd5b82525081516020918201929091019080838360005b83811015620000b85781810151838201526020016200009e565b50505050905090810190601f168015620000e65780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200010a57600080fd5b9083019060208201858111156200012057600080fd5b82516401000000008111828201881017156200013b57600080fd5b82525081516020918201929091019080838360005b838110156200016a57818101518382015260200162000150565b50505050905090810190601f168015620001985780820380516001836020036101000a031916815260200191505b506040525050600060208181527f67be87c3ff9960ca1e9cfac5cab2ff4747269cf9ed20c9b7306235ac35a491c5805460ff1990811660019081179092557ff7815fccbf112960a73756e185887fedcb9fc64ca0a16cc5923b7960ed78080080548216831790557f77b7bbe0e49b76487c9476b5db3354cf5270619d0037ccb899c2a4c4a75b43188054821683179055635b5e139f60e01b9093527f9562381dfbc2d8b8b66e765249f330164b73e329e5f01670660643571d1974df805490931617909155600c80546001600160a01b031916331790558351620002839250600991850190620002a2565b5080516200029990600a906020840190620002a2565b5050506200033e565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002e557805160ff191683800117855562000315565b8280016001018555821562000315579182015b8281111562000315578251825591602001919060010190620002f8565b506200032392915062000327565b5090565b5b8082111562000323576000815560010162000328565b612042806200034e6000396000f3fe608060405234801561001057600080fd5b50600436106101425760003560e01c806370a08231116100b8578063b88d4fde1161007c578063b88d4fde146103db578063c87b56dd1461046b578063d3fc986414610488578063e985e9c51461050d578063f2fde38b1461053b578063f3fe3bc31461056157610142565b806370a082311461036f578063860d248a146103955780638da5cb5b1461039d57806395d89b41146103a5578063a22cb465146103ad57610142565b806323b872dd1161010a57806323b872dd146102805780632f745c59146102b657806342842e0e146102e257806342966c68146103185780634f6ccce7146103355780636352211e1461035257610142565b806301ffc9a71461014757806306fdde0314610182578063081812fc146101ff578063095ea7b31461023857806318160ddd14610266575b600080fd5b61016e6004803603602081101561015d57600080fd5b50356001600160e01b031916610569565b604080519115158252519081900360200190f35b61018a610588565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101c45781810151838201526020016101ac565b50505050905090810190601f1680156101f15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61021c6004803603602081101561021557600080fd5b503561061e565b604080516001600160a01b039092168252519081900360200190f35b6102646004803603604081101561024e57600080fd5b506001600160a01b038135169060200135610702565b005b61026e61092e565b60408051918252519081900360200190f35b6102646004803603606081101561029657600080fd5b506001600160a01b03813581169160208101359091169060400135610934565b61026e600480360360408110156102cc57600080fd5b506001600160a01b038135169060200135610ba7565b610264600480360360608110156102f857600080fd5b506001600160a01b03813581169160208101359091169060400135610c66565b6102646004803603602081101561032e57600080fd5b5035610c86565b61026e6004803603602081101561034b57600080fd5b5035610d0a565b61021c6004803603602081101561036857600080fd5b5035610d9a565b61026e6004803603602081101561038557600080fd5b50356001600160a01b0316610e20565b61018a610ea5565b61021c610ec7565b61018a610ed6565b610264600480360360408110156103c357600080fd5b506001600160a01b0381351690602001351515610f37565b610264600480360360808110156103f157600080fd5b6001600160a01b0382358116926020810135909116916040820135919081019060808101606082013564010000000081111561042c57600080fd5b82018360208201111561043e57600080fd5b8035906020019184600183028401116401000000008311171561046057600080fd5b509092509050610fa5565b61018a6004803603602081101561048157600080fd5b5035610fee565b6102646004803603606081101561049e57600080fd5b6001600160a01b03823516916020810135918101906060810160408201356401000000008111156104ce57600080fd5b8201836020820111156104e057600080fd5b8035906020019184600183028401116401000000008311171561050257600080fd5b509092509050611118565b61016e6004803603604081101561052357600080fd5b506001600160a01b03813581169160200135166111e0565b6102646004803603602081101561055157600080fd5b50356001600160a01b031661120e565b61018a611355565b6001600160e01b03191660009081526020819052604090205460ff1690565b60098054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106145780601f106105e957610100808354040283529160200191610614565b820191906000526020600020905b8154815290600101906020018083116105f757829003601f168201915b5050505050905090565b6000818152600160209081526040808320548151808301909252600682526518181998181960d11b9282019290925283916001600160a01b03166106e05760405162461bcd60e51b81526004018080602001828103825283818151815260200191508051906020019080838360005b838110156106a557818101518382015260200161068d565b50505050905090810190601f1680156106d25780820380516001836020036101000a031916815260200191505b509250505060405180910390fd5b506000838152600260205260409020546001600160a01b031691505b50919050565b60008181526001602052604090205481906001600160a01b03163381148061074d57506001600160a01b038116600090815260046020908152604080832033845290915290205460ff165b6040518060400160405280600681526020016530303330303360d01b815250906107b85760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600083815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091528491906001600160a01b03166108405760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600084815260016020908152604091829020548251808401909352600683526506060666060760d31b918301919091526001600160a01b03908116919087168214156108ce5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5060008581526002602052604080822080546001600160a01b0319166001600160a01b038a811691821790925591518893918516917f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92591a4505050505050565b60055490565b60008181526001602052604090205481906001600160a01b03163381148061097257506000828152600260205260409020546001600160a01b031633145b806109a057506001600160a01b038116600090815260046020908152604080832033845290915290205460ff165b604051806040016040528060068152602001650c0c0ccc0c0d60d21b81525090610a0b5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600083815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091528491906001600160a01b0316610a935760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600084815260016020908152604091829020548251808401909352600683526530303330303760d01b918301919091526001600160a01b03908116919088168214610b205760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5060408051808201909152600681526530303330303160d01b60208201526001600160a01b038716610b935760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50610b9e8686611377565b50505050505050565b6001600160a01b0382166000908152600760209081526040808320548151808301909252600682526530303530303760d01b92820192909252908310610c2e5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b506001600160a01b0383166000908152600760205260409020805483908110610c5357fe5b9060005260206000200154905092915050565b610c81838383604051806020016040528060008152506113f2565b505050565b600c5460408051808201909152600681526530313830303160d01b6020820152906001600160a01b03163314610cfd5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50610d07816117ff565b50565b60055460408051808201909152600681526530303530303760d01b60208201526000918310610d7a5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5060058281548110610d8857fe5b90600052602060002001549050919050565b600081815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091526001600160a01b031690816106fc5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b60408051808201909152600681526530303330303160d01b60208201526000906001600160a01b038316610e955760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50610e9f82611846565b92915050565b6040518060400160405280600681526020016518189c18181960d11b81525081565b600c546001600160a01b031681565b600a8054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156106145780601f106105e957610100808354040283529160200191610614565b3360008181526004602090815260408083206001600160a01b03871680855290835292819020805460ff1916861515908117909155815190815290519293927f17307eab39ab6107e8899845ad3d59bd9653f200f220920489ca2b5937696c31929181900390910190a35050565b610fe785858585858080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152506113f292505050565b5050505050565b600081815260016020908152604091829020548251808401909352600683526518181998181960d11b9183019190915260609183916001600160a01b03166110775760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b506000838152600b602090815260409182902080548351601f60026000196101006001861615020190931692909204918201849004840281018401909452808452909183018282801561110b5780601f106110e05761010080835404028352916020019161110b565b820191906000526020600020905b8154815290600101906020018083116110ee57829003601f168201915b5050505050915050919050565b600c5460408051808201909152600681526530313830303160d01b6020820152906001600160a01b0316331461118f5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5061119a8484611851565b6111da8383838080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152506118a592505050565b50505050565b6001600160a01b03918216600090815260046020908152604080832093909416825291909152205460ff1690565b600c5460408051808201909152600681526530313830303160d01b6020820152906001600160a01b031633146112855760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5060408051808201909152600681526518189c18181960d11b60208201526001600160a01b0382166112f85760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600c546040516001600160a01b038084169216907f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e090600090a3600c80546001600160a01b0319166001600160a01b0392909216919091179055565b6040518060400160405280600681526020016530313830303160d01b81525081565b6000818152600160205260409020546001600160a01b03166113988261194c565b6113a28183611987565b6113ac8383611995565b81836001600160a01b0316826001600160a01b03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef60405160405180910390a4505050565b60008281526001602052604090205482906001600160a01b03163381148061143057506000828152600260205260409020546001600160a01b031633145b8061145e57506001600160a01b038116600090815260046020908152604080832033845290915290205460ff165b604051806040016040528060068152602001650c0c0ccc0c0d60d21b815250906114c95760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600084815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091528591906001600160a01b03166115515760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600085815260016020908152604091829020548251808401909352600683526530303330303760d01b918301919091526001600160a01b039081169190891682146115de5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5060408051808201909152600681526530303330303160d01b60208201526001600160a01b0388166116515760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b5061165c8787611377565b61166e876001600160a01b031661199f565b156117f5576000876001600160a01b031663150b7a02338b8a8a6040518563ffffffff1660e01b815260040180856001600160a01b03168152602001846001600160a01b0316815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b838110156116f85781810151838201526020016116e0565b50505050905090810190601f1680156117255780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b15801561174757600080fd5b505af115801561175b573d6000803e3d6000fd5b505050506040513d602081101561177157600080fd5b505160408051808201909152600681526530303330303560d01b60208201529091506001600160e01b03198216630a85bd0160e11b146117f25760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50505b5050505050505050565b611808816119db565b6000818152600b60205260409020546002600019610100600184161502019091160415610d07576000818152600b60205260408120610d0791611f35565b6000610e9f82611a73565b61185b8282611a8e565b600580546001810182557f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db00182905554600091825260066020526040909120600019909101905550565b600082815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091528391906001600160a01b031661192c5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b506000838152600b6020908152604090912083516111da92850190611f79565b6000818152600260205260409020546001600160a01b031615610d0757600090815260026020526040902080546001600160a01b0319169055565b6119918282611bcd565b5050565b6119918282611d5d565b6000813f7fc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a47081158015906119d35750808214155b949350505050565b6119e481611e45565b60008181526006602052604081205460058054919260001983019290919083908110611a0c57fe5b906000526020600020015490508060058481548110611a2757fe5b6000918252602090912001556005805480611a3e57fe5b600082815260208082208301600019908101839055909201909255918152600690915260408082209390935592835250812055565b6001600160a01b031660009081526007602052604090205490565b60408051808201909152600681526530303330303160d01b60208201526001600160a01b038316611b005760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600081815260016020908152604091829020548251808401909352600683526518181998181b60d11b918301919091526001600160a01b031615611b865760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50611b918282611995565b60405181906001600160a01b038416906000907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908290a45050565b600081815260016020908152604091829020548251808401909352600683526530303330303760d01b918301919091526001600160a01b03848116911614611c565760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600081815260016020908152604080832080546001600160a01b031916905560088252808320546001600160a01b038616845260079092529091205460001901808214611d20576001600160a01b0384166000908152600760205260408120805483908110611cc257fe5b906000526020600020015490508060076000876001600160a01b03166001600160a01b031681526020019081526020016000208481548110611d0057fe5b600091825260208083209091019290925591825260089052604090208290555b6001600160a01b0384166000908152600760205260409020805480611d4157fe5b6001900381819060005260206000200160009055905550505050565b600081815260016020908152604091829020548251808401909352600683526518181998181b60d11b918301919091526001600160a01b031615611de25760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b50600081815260016020818152604080842080546001600160a01b0319166001600160a01b039790971696871790559483526007815284832080549283018155808452818420909201849055905492825260089052919091206000199091019055565b600081815260016020908152604091829020548251808401909352600683526518181998181960d11b918301919091528291906001600160a01b0316611ecc5760405162461bcd60e51b81526020600482018181528351602484015283519092839260449091019190850190808383600083156106a557818101518382015260200161068d565b506000828152600160205260409020546001600160a01b0316611eee8361194c565b611ef88184611987565b60405183906000906001600160a01b038416907fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef908390a4505050565b50805460018160011615610100020316600290046000825580601f10611f5b5750610d07565b601f016020900490600052602060002090810190610d079190611ff7565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611fba57805160ff1916838001178555611fe7565b82800160010185558215611fe7579182015b82811115611fe7578251825591602001919060010190611fcc565b50611ff3929150611ff7565b5090565b5b80821115611ff35760008155600101611ff856fea264697066735822122020a69831308ace58439c7773bdc620e619f5b4a53b0685c8707f10a3beb989ea64736f6c634300060c0033";

    public static final String FUNC_CANNOT_TRANSFER_TO_ZERO_ADDRESS = "CANNOT_TRANSFER_TO_ZERO_ADDRESS";

    public static final String FUNC_NOT_CURRENT_OWNER = "NOT_CURRENT_OWNER";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_GETAPPROVED = "getApproved";

    public static final String FUNC_ISAPPROVEDFORALL = "isApprovedForAll";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_OWNEROF = "ownerOf";

    public static final String FUNC_SAFETRANSFERFROM = "safeTransferFrom";

    public static final String FUNC_SETAPPROVALFORALL = "setApprovalForAll";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";

    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";

    public static final String FUNC_TOKENURI = "tokenURI";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }, new TypeReference<Uint256>(true) {
    }));

    ;

    public static final Event APPROVALFORALL_EVENT = new Event("ApprovalForAll", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }, new TypeReference<Bool>() {
    }));

    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }));

    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {
    }, new TypeReference<Address>(true) {
    }, new TypeReference<Uint256>(true) {
    }));

    ;

    protected Erc721Contract(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    protected Erc721Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Erc721Contract> deploy(Web3j web3j, Credentials credentials, GasProvider contractGasProvider, String _name, String _symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Utf8String(_name), new Utf8String(_symbol)));
        return deployRemoteCall(Erc721Contract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Erc721Contract> deploy(Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider, String _name, String _symbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.asList(new Utf8String(_name), new Utf8String(_symbol)));
        return deployRemoteCall(Erc721Contract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = log;
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._approved = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            return typedResponse;
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public List<ApprovalForAllEventResponse> getApprovalForAllEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVALFORALL_EVENT, transactionReceipt);
        ArrayList<ApprovalForAllEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVALFORALL_EVENT, log);
            ApprovalForAllEventResponse typedResponse = new ApprovalForAllEventResponse();
            typedResponse.log = log;
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._operator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._approved = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            return typedResponse;
        });
    }

    public Observable<ApprovalForAllEventResponse> approvalForAllEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVALFORALL_EVENT));
        return approvalForAllEventObservable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = log;
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            return typedResponse;
        });
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventObservable(filter);
    }

    @Override
    public List<ErcTxEvent> getTxEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<ErcTxEvent> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ErcTxEvent response = new ErcTxEvent();
            response.setLog(eventValues.getLog());
            response.setFrom((String) eventValues.getIndexedValues().get(0).getValue());
            response.setTo((String) eventValues.getIndexedValues().get(1).getValue());
            response.setValue((BigInteger) eventValues.getIndexedValues().get(2).getValue());
            responses.add(response);
        }
        return responses;
    }

    public Observable<ErcTxEvent> transferEventObservable(PlatonFilter filter) {
        return web3j.platonLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
            ErcTxEvent response = new ErcTxEvent();
            response.setLog(log);
            response.setFrom((String) eventValues.getIndexedValues().get(0).getValue());
            response.setTo((String) eventValues.getIndexedValues().get(1).getValue());
            response.setValue((BigInteger) eventValues.getIndexedValues().get(2).getValue());
            return response;
        });
    }

    public Observable<ErcTxEvent> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        PlatonFilter filter = new PlatonFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public RemoteCall<String> CANNOT_TRANSFER_TO_ZERO_ADDRESS() {
        final Function function = new Function(FUNC_CANNOT_TRANSFER_TO_ZERO_ADDRESS, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> NOT_CURRENT_OWNER() {
        final Function function = new Function(FUNC_NOT_CURRENT_OWNER, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _approved, BigInteger _tokenId) {
        final Function function = new Function(FUNC_APPROVE, Arrays.asList(new Address(_approved), new Uint256(_tokenId)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, Collections.singletonList(new Address(_owner)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _tokenId) {
        final Function function = new Function(FUNC_BURN, Collections.singletonList(new Uint256(_tokenId)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getApproved(BigInteger _tokenId) {
        final Function function = new Function(FUNC_GETAPPROVED, Collections.singletonList(new Uint256(_tokenId)), Collections.singletonList(new TypeReference<Address>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isApprovedForAll(String _owner, String _operator) {
        final Function function = new Function(FUNC_ISAPPROVEDFORALL, Arrays.asList(new Address(_owner), new Address(_operator)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> mint(String _to, BigInteger _tokenId, String _uri) {
        final Function function = new Function(FUNC_MINT, Arrays.asList(new Address(_to), new Uint256(_tokenId), new Utf8String(_uri)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, Collections.emptyList(), Collections.singletonList(new TypeReference<Address>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> ownerOf(BigInteger _tokenId) {
        final Function function = new Function(FUNC_OWNEROF, Collections.singletonList(new Uint256(_tokenId)), Collections.singletonList(new TypeReference<Address>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(String _from, String _to, BigInteger _tokenId) {
        final Function function = new Function(FUNC_SAFETRANSFERFROM, Arrays.asList(new Address(_from), new Address(_to), new Uint256(_tokenId)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> safeTransferFrom(String _from, String _to, BigInteger _tokenId, byte[] _data) {
        final Function function = new Function(FUNC_SAFETRANSFERFROM, Arrays.asList(new Address(_from), new Address(_to), new Uint256(_tokenId), new com.platon.abi.solidity.datatypes.DynamicBytes(_data)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setApprovalForAll(String _operator, Boolean _approved) {
        final Function function = new Function(FUNC_SETAPPROVALFORALL, Arrays.asList(new Address(_operator), new Bool(_approved)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> supportsInterface(byte[] _interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, Collections.singletonList(new Bytes4(_interfaceID)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Override
    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<BigInteger> decimals() {
        // ERC721没有精度，所以此处返回0
        return new RemoteCall<>(() -> BigInteger.ZERO);
    }

    public RemoteCall<BigInteger> tokenByIndex(BigInteger _index) {
        final Function function = new Function(FUNC_TOKENBYINDEX, Collections.singletonList(new Uint256(_index)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> tokenOfOwnerByIndex(String _owner, BigInteger _index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX, Arrays.asList(new Address(_owner), new Uint256(_index)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getTokenURI(BigInteger _tokenId) {
        final Function function = new Function(FUNC_TOKENURI, Collections.singletonList(new Uint256(_tokenId)), Collections.singletonList(new TypeReference<Utf8String>() {
        }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, Collections.emptyList(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _tokenId) {
        final Function function = new Function(FUNC_TRANSFERFROM, Arrays.asList(new Address(_from), new Address(_to), new Uint256(_tokenId)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(FUNC_TRANSFEROWNERSHIP, Collections.singletonList(new Address(_newOwner)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static Erc721Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider) {
        return new Erc721Contract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Erc721Contract load(String contractAddress, Web3j web3j, Credentials credentials, GasProvider contractGasProvider, BigInteger blockNumber) {
        Erc721Contract erc721Contract = new Erc721Contract(contractAddress, web3j, credentials, contractGasProvider);
        erc721Contract.setDefaultBlockParameter(DefaultBlockParameter.valueOf(blockNumber));
        return erc721Contract;
    }

    public static Erc721Contract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, GasProvider contractGasProvider) {
        return new Erc721Contract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class ApprovalEventResponse {

        public Log log;

        public String _owner;

        public String _approved;

        public BigInteger _tokenId;

    }

    public static class ApprovalForAllEventResponse {

        public Log log;

        public String _owner;

        public String _operator;

        public Boolean _approved;

    }

    public static class OwnershipTransferredEventResponse {

        public Log log;

        public String previousOwner;

        public String newOwner;

    }

}
