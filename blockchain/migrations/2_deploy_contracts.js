const CarPooling = artifacts.require("CarPooling");

module.exports = function(deployer) {
  deployer.deploy(CarPooling);
};