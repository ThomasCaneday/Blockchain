import java.util.ArrayList;


/**
 * This holds the values for a Block in the Blockchain, and it has methods to compute the Merkle Root and generate a hash.
 */
public class Block {


    private String sMerkleRoot;
    private int iDifficulty = 5; // Mining seconds in testing 5: 6,10,15,17,20,32 | testing 6: 12,289,218
    private String sNonce;
    private String sMinerUsername;
    private String sHash;



    /**
     * This computes the Merkle Root. It either accepts 2 or 4 items, or if made to be dynamic, then accepts any
     * multiple of 2 (2,4,8.16.32,etc.) items.
     * @param lstItems
     * @return
     */
    public synchronized String computeMerkleRoot(ArrayList<String> lstItems) {

        BlockchainUtil oBlock = new BlockchainUtil();
        ArrayList<String> arrayList = new ArrayList();

        int lSize = lstItems.size();

        MerkleNode oNode1 = new MerkleNode();
        MerkleNode oNode2 = new MerkleNode();
        MerkleNode oNode3 = new MerkleNode();

        if(lSize == 0) {
            return "Not enough items in list.";
        }

        while(lSize != 1) {
            if((lSize % 2) != 0) {
                return "Items in list are not a power of 2.";
            }
            lSize /= 2;

        }

        for(int i = 0; i < lstItems.size(); i += 2) {

            oNode1.sHash = oBlock.generateHash(lstItems.get(i));
            oNode2.sHash = oBlock.generateHash(lstItems.get(i + 1));

            oNode3.sHash = oNode1.sHash + oNode2.sHash;
            arrayList.add(oNode3.sHash);
        }

        if(arrayList.size() > 1) {
            return computeMerkleRoot(arrayList);
        }
        populateMerkleNode(oNode3, oNode1, oNode2);
        arrayList.add(oNode3.sHash);
        //System.out.println("Merkle root method for " + lstItems.size() + " items worked!");
        return arrayList.get(1);


//
//        if(lSize == 1) {
//            MerkleNode oNode1 = new MerkleNode();
//            MerkleNode oNode2 = new MerkleNode();
//            MerkleNode oNode3 = new MerkleNode();
//
//
//        }

/********************** Tried manipulating passed in arraylist below to make size a power of 2

//        while loop to check if power of 2
        while((lSize % 2) == 0) {
            lSize /= 2;
        }
//        Power of two control flow
        if(lSize == 1) {
            for(int i = 0; i < lstItems.size(); i++){
                MerkleNode oNode = new MerkleNode(lstItems.get(i));
            }
        }
//        Not power of two control flow, recursive call on method
        else {
            System.out.println("List of items is not power of 2, removing two from list.");
            lstItems.remove(-1);
            lstItems.remove(-1);
            computeMerkleRoot(lstItems);
        }


        while(lSize != 1) {
            System.out.println("List of items is not power of 2, removing one from list.");
            lstItems.remove(-1);
        }

        while(lstItems != null) {
            if(lSize == 1) {
                for(int i = lstItems.size(); i > 0; i--) {

                }
            } else {
                System.out.println("List of items is not power of 2, removing one from list.");
                lstItems.remove(-1);
//                oBlock.computeMerkleRoot(lstItems);
            }
        }
         ***********************/

    }



    /**
     * This method populates a Merkle node's left, right, and hash variables.
     * @param oNode
     * @param oLeftNode
     * @param oRightNode
     */
    private void populateMerkleNode(MerkleNode oNode, MerkleNode oLeftNode, MerkleNode oRightNode){
        oNode.oLeft = oLeftNode;
        oNode.oRight = oRightNode;
        oNode.sHash = BlockchainUtil.generateHash(oNode.oLeft.sHash + oNode.oRight.sHash);
    }


    // Hash this block, and hash will also be next block's previous hash.

    /**
     * This generates the hash for this block by combining the properties and hashing them.
     * @return
     */
    public String computeHash() {

        return new BlockchainUtil().generateHash(sMerkleRoot + iDifficulty + sMinerUsername + sNonce);
    }



    public int getDifficulty() {
        return iDifficulty;
    }
    public void setDifficulty(int iDifficulty) {
		this.iDifficulty = iDifficulty;
	}


    public String getNonce() {
        return sNonce;
    }
    public void setNonce(String nonce) {
        this.sNonce = nonce;
    }

    public void setMinerUsername(String sMinerUsername) {
        this.sMinerUsername = sMinerUsername;
    }

    public String getHash() { return sHash; }
    public void setHash(String h) {
        this.sHash = h;
    }

    public synchronized void setMerkleRoot(String merkleRoot) { this.sMerkleRoot = merkleRoot; }




    /**
     * Run this to test your merkle tree logic.
     * @param args
     */
    public static void main(String[] args){

        ArrayList<String> lstItems = new ArrayList<>();
        Block oBlock = new Block();
        String sMerkleRoot;

        // These merkle root hashes based on "t1","t2" for two items, and then "t3","t4" added for four items.
        String sExpectedMerkleRoot_2Items = "3269f5f93615478d3d2b4a32023126ff1bf47ebc54c2c96651d2ac72e1c5e235";
        String sExpectedMerkleRoot_4Items = "e08f7b0331197112ff8aa7acdb4ecab1cfb9497cbfb84fb6d54f1cfdb0579d69";
        String sExpectedMerkleRoot_8Items = "36941ee71cbbb9196c4691026e68c8be5605332842a766bf069d631d2c7c8d47";

        lstItems.add("t1");
        lstItems.add("t2");


        // *** BEGIN TEST 2 ITEMS ***

        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_2Items)){

            System.out.println("Merkle root method for 2 items worked!");
        }

        else{
            System.out.println("Merkle root method for 2 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_2Items);
            System.out.println("Received: " + sMerkleRoot);

        }


        // *** BEGIN TEST 4 ITEMS ***

        lstItems.add("t3");
        lstItems.add("t4");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_4Items)){

            System.out.println("Merkle root method for 4 items worked!");
        }

        else{
            System.out.println("Merkle root method for 4 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_4Items);
            System.out.println("Received: " + sMerkleRoot);

        }

        // 8 items
        lstItems.add("t5");
        lstItems.add("t6");
        lstItems.add("t7");
        lstItems.add("t8");
        sMerkleRoot = oBlock.computeMerkleRoot(lstItems);

        if(sMerkleRoot.equals(sExpectedMerkleRoot_8Items)){
            System.out.println("Don't play games with me son! Merkle root method for 8 items worked! I am him");
        }

        else{
            System.out.println("Dude you suck... Your merkle root method for 8 items failed!");
            System.out.println("Expected: " + sExpectedMerkleRoot_8Items);
            System.out.println("Received: " + sMerkleRoot);

        }
    }
}
