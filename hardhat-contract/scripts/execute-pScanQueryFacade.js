const { ethers } = require("hardhat");

async function main() {
  const [owner, chendai1] = await ethers.getSigners();
  console.log("owner account:", owner.address);

  const contractArtifact = await artifacts.readArtifact("PScanQueryFacade");
  const contract = new ethers.Contract('0x3b1Cb858b0C59E9d3e7BFccA41e7E0F2eD35313d', contractArtifact.abi, owner);

  // info721 = await contract.erc721Info('0x35481c7e36FF3913583599cAc161E1f66544D489');
  // console.log("info721 :", info721);

  info20 = await contract.erc20Info('0x46195B2E97a0E3d097c2DEA20313B2C2C58ac6EC');
  console.log("info20 :", info20);
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });