import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by mrampiah on 11/16/16.
 */
public class CustomConnection implements XMPPConnection{
    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getUser() {
        return null;
    }

    @Override
    public String getStreamId() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public boolean isSecureConnection() {
        return false;
    }

    @Override
    public boolean isUsingCompression() {
        return false;
    }

    @Override
    public void sendPacket(Stanza packet) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendStanza(Stanza stanza) throws SmackException.NotConnectedException {

    }

    @Override
    public void send(PlainStreamElement element) throws SmackException.NotConnectedException {

    }

    @Override
    public void addConnectionListener(ConnectionListener connectionListener) {

    }

    @Override
    public void removeConnectionListener(ConnectionListener connectionListener) {

    }

    @Override
    public PacketCollector createPacketCollectorAndSend(IQ packet) throws SmackException.NotConnectedException {
        return null;
    }

    @Override
    public PacketCollector createPacketCollectorAndSend(StanzaFilter packetFilter, Stanza packet) throws SmackException.NotConnectedException {
        return null;
    }

    @Override
    public PacketCollector createPacketCollector(StanzaFilter packetFilter) {
        return null;
    }

    @Override
    public PacketCollector createPacketCollector(PacketCollector.Configuration configuration) {
        return null;
    }

    @Override
    public void removePacketCollector(PacketCollector collector) {

    }

    @Override
    public void addPacketListener(StanzaListener packetListener, StanzaFilter packetFilter) {

    }

    @Override
    public boolean removePacketListener(StanzaListener packetListener) {
        return false;
    }

    @Override
    public void addSyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {

    }

    @Override
    public boolean removeSyncStanzaListener(StanzaListener packetListener) {
        return false;
    }

    @Override
    public void addAsyncStanzaListener(StanzaListener packetListener, StanzaFilter packetFilter) {

    }

    @Override
    public boolean removeAsyncStanzaListener(StanzaListener packetListener) {
        return false;
    }

    @Override
    public void addPacketSendingListener(StanzaListener packetListener, StanzaFilter packetFilter) {

    }

    @Override
    public void removePacketSendingListener(StanzaListener packetListener) {

    }

    @Override
    public void addPacketInterceptor(StanzaListener packetInterceptor, StanzaFilter packetFilter) {

    }

    @Override
    public void removePacketInterceptor(StanzaListener packetInterceptor) {

    }

    @Override
    public long getPacketReplyTimeout() {
        return 0;
    }

    @Override
    public void setPacketReplyTimeout(long timeout) {

    }

    @Override
    public int getConnectionCounter() {
        return 0;
    }

    @Override
    public void setFromMode(FromMode fromMode) {

    }

    @Override
    public FromMode getFromMode() {
        return null;
    }

    @Override
    public <F extends ExtensionElement> F getFeature(String element, String namespace) {
        return null;
    }

    @Override
    public boolean hasFeature(String element, String namespace) {
        return false;
    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback, ExceptionCallback exceptionCallback) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendStanzaWithResponseCallback(Stanza stanza, StanzaFilter replyFilter, StanzaListener callback, ExceptionCallback exceptionCallback, long timeout) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback) throws SmackException.NotConnectedException {

    }

    @Override
    public void sendIqWithResponseCallback(IQ iqRequest, StanzaListener callback, ExceptionCallback exceptionCallback, long timeout) throws SmackException.NotConnectedException {

    }

    @Override
    public void addOneTimeSyncCallback(StanzaListener callback, StanzaFilter packetFilter) {

    }

    @Override
    public IQRequestHandler registerIQRequestHandler(IQRequestHandler iqRequestHandler) {
        return null;
    }

    @Override
    public IQRequestHandler unregisterIQRequestHandler(IQRequestHandler iqRequestHandler) {
        return null;
    }

    @Override
    public IQRequestHandler unregisterIQRequestHandler(String element, String namespace, IQ.Type type) {
        return null;
    }

    @Override
    public long getLastStanzaReceived() {
        return 0;
    }
}
