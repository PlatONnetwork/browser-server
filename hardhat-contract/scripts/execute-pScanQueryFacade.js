const { ethers } = require("hardhat");

async function main() {
  const [owner] = await ethers.getSigners();
  console.log("owner account:", owner.address);

  const contractArtifact = await artifacts.readArtifact("PScanQueryFacade");
  const contract = new ethers.Contract('0x00cdd08793b753506A0C4a9958146427D3F648f2', contractArtifact.abi, owner);


  info = await contract.ercInfo('0x12aD9058A824C06f29Aa3fae7765F89BD77C6C05');
  console.log("info contract :", info);

  info = await contract.ercInfo('0x2905F311530Bf3A11aF0BeFc386E88e381d600c0');
  console.log("info address :", info);

  info = await contract.ercInfo('0x46195B2E97a0E3d097c2DEA20313B2C2C58ac6EC');
  console.log("info ERC20 :", info);

  info = await contract.ercInfo('0xDdEAc14f4Ac49132a9A6d4a8f7a393a0f42CaCcF');
  console.log("info ERC721 :", info);

  info = await contract.ercInfo('0x35481c7e36FF3913583599cAc161E1f66544D489');
  console.log("info ERC721MetaData :", info);

  info = await contract.ercInfo('0x41F288BB5dCAb0f0E653e6405954d4DcB386b270');
  console.log("info ERC721MetaDataEnumerable :", info);

  info = await contract.ercInfo('0xeDDf56086323c51458b5939d3459c3b3bE8B31F1');
  console.log("info ERC1155 :", info);

  info = await contract.ercInfo('0x8807E0a3836Cd0a2911d2094dc2C19108D1D37c5');
  console.log("info ERC1155MetaData :", info);

  info = await contract.ercInfo('0xCC0A4a47ccab3F9936DEbb4EA6AeC0ae61d303FD');
  console.log("info ERC1155Customize :", info);

}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });