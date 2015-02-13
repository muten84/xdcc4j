package it.luigibifulco.xdcc4j.search.cache;

import it.luigibifulco.xdcc4j.GuiceJUnitRunner;
import it.luigibifulco.xdcc4j.GuiceJUnitRunner.GuiceModules;
import it.luigibifulco.xdcc4j.search.SearchModule;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceModules(SearchModule.class)
public class XdccCacheTest {

	@Inject
	private XdccCache cache;

	@Test
	public final void testCacheFromRemoteUserAndPersist() {
		Assert.assertTrue(cache.cache().size() == 0);
		Assert.assertTrue(cache.cacheFrom("irc.uragano.org", "#sunshine",
				"SUN|DVDRIP|23"));
		Assert.assertTrue(cache.persistCache());
		int size = cache.cache().size();
		Assert.assertTrue(size > 0);
		cache.clearCache();
		Assert.assertTrue(cache.cache().size() == 0);
		Assert.assertTrue(cache.cacheFromLocal());
		Assert.assertTrue(cache.cache().size() == size);

	}

}
