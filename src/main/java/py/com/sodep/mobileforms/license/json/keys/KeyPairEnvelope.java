package py.com.sodep.mobileforms.license.json.keys;

public class KeyPairEnvelope {

	private KeyEnvelope publicKey;

	private KeyEnvelope privateKey;

	public KeyEnvelope getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(KeyEnvelope publicKey) {
		this.publicKey = publicKey;
	}

	public KeyEnvelope getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(KeyEnvelope privateKey) {
		this.privateKey = privateKey;
	}

}
